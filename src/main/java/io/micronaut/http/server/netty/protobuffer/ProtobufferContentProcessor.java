package io.micronaut.http.server.netty.protobuffer;

import com.google.protobuf.Message;
import io.micronaut.core.async.subscriber.TypedSubscriber;
import io.micronaut.core.type.Argument;
import io.micronaut.http.codec.ProtobufferBuilderCreator;
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
import java.util.Objects;
import java.util.Optional;

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
            builder = ProtobufferBuilderCreator.getMessageBuilder(clazz)
                    .orElseThrow(() -> new IllegalStateException("Unable to create message builder!"));
        } else {
            throw new IllegalArgumentException("This is not handled yet!");
        }
        super.doOnSubscribe(subscription, subscriber);
    }

    @Override
    protected void doOnComplete() {
        message = builder.build();
        super.doOnComplete();
    }
}
