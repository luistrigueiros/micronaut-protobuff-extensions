package io.micronaut.http.server.netty.protobuffer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import io.micronaut.core.async.subscriber.TypedSubscriber;
import io.micronaut.core.reflect.ClassUtils;
import io.micronaut.core.type.Argument;
import io.micronaut.http.codec.ProtobufferBuilderCreator;
import io.micronaut.http.codec.ProtobufferCodec;
import io.micronaut.http.server.HttpServerConfiguration;
import io.micronaut.http.server.netty.AbstractHttpContentProcessor;
import io.micronaut.http.server.netty.NettyHttpRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ReferenceCountUtil;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

public class ProtobufferContentProcessor extends AbstractHttpContentProcessor<Message> {
    /**
     * Incomming byte array
     */
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    /**
     *
     */
    private Message.Builder builder;
    /**
     *
     */
    private Message message;

    /**
     * @param nettyHttpRequest The {@link NettyHttpRequest}
     * @param configuration    The {@link HttpServerConfiguration}
     */
    public ProtobufferContentProcessor(NettyHttpRequest<?> nettyHttpRequest, HttpServerConfiguration configuration) {
        super(nettyHttpRequest, configuration);
    }

    @Override
    protected void onData(ByteBufHolder message) {
        ByteBuf content = message.content();
        try {
            byte[] bytes = ByteBufUtil.getBytes(content);
            byteArrayOutputStream.write(bytes);
        } catch (IOException e) {
            //Does nothing
        } finally {
            ReferenceCountUtil.release(content);
        }
    }

    @Override
    protected void doOnSubscribe(Subscription subscription, Subscriber<? super Message> subscriber) {
        if (parentSubscription == null) {
            return;
        }
        if (subscriber instanceof TypedSubscriber) {
            TypedSubscriber typedSubscriber = (TypedSubscriber) subscriber;
            Argument typeArgument = typedSubscriber.getTypeArgument();
            Class targetType = typeArgument.getType();

            Class<? extends Message> clazz = (Class<? extends Message>) targetType;
            Optional<Message.Builder> messageBuilder = ProtobufferBuilderCreator.getMessageBuilder(clazz);
            if (!messageBuilder.isPresent()) {
                subscriber.onError(new IllegalStateException("Unable to create message builder!"));
                return;
            }
            builder = messageBuilder.get();
        } else {
            if (!nettyHttpRequest.getHeaders().contains(ProtobufferCodec.X_PROTOBUF_MESSAGE_HEADER)) {
                buildMissingRequiredHeaderError(subscriber);
                return;
            }
            String fullyQualifiedType = nettyHttpRequest.getHeaders().get(ProtobufferCodec.X_PROTOBUF_MESSAGE_HEADER);
            Optional<Class> aClass = ClassUtils.forName(fullyQualifiedType, null);
            if (!aClass.isPresent()) {
                subscriber.onError(new IllegalArgumentException(format("The given message type[%s]  is not found", fullyQualifiedType)));
                return;
            }
            Optional<Message.Builder> messageBuilder = ProtobufferBuilderCreator.getMessageBuilder(aClass.get());
            if (!messageBuilder.isPresent()) {
                subscriber.onError(new IllegalStateException("Unable to create message builder!"));
                return;
            }
            builder = messageBuilder.get();
        }
        super.doOnSubscribe(subscription, subscriber);
    }

    private static void buildMissingRequiredHeaderError(Subscriber<? super Message> subscriber) {
        String mgs = format("Need the HTTP Header[%s] when the request in untyped", ProtobufferCodec.X_PROTOBUF_MESSAGE_HEADER);
        IllegalArgumentException exception = new IllegalArgumentException(mgs);
        subscriber.onError(exception);
    }

    @Override
    protected void doOnComplete() {
        try {
            builder.mergeFrom(byteArrayOutputStream.toByteArray(), ProtobufferBuilderCreator.extensionRegistry);
            message = builder.build();
            super.doOnComplete();
        }catch (InvalidProtocolBufferException e) {
            super.doOnError(e);
        }
    }
}
