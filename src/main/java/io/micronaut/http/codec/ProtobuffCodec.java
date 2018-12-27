package io.micronaut.http.codec;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.core.io.buffer.ByteBufferFactory;
import io.micronaut.core.type.Argument;
import io.micronaut.http.MediaType;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ProtobuffCodec implements MediaTypeCodec {

    private static final ConcurrentHashMap<Class<?>, Method> methodCache = new ConcurrentHashMap<>();

    private final ExtensionRegistry extensionRegistry = ExtensionRegistry.newInstance();
    /**
     * Protobuffer encoded data: application/x-protobuf.
     */
    public static final String PROTOBUFFER_ENCODED = "application/x-protobuf";

    /**
     * Protobuffer encoded data: application/x-www-form-urlencoded.
     */
    public static final MediaType PROTOBUFFER_ENCODED_TYPE = new MediaType(PROTOBUFFER_ENCODED);

    @Override
    public boolean supportsType(Class<?> type) {
        return Message.class.isAssignableFrom(type);
    }

    @Override
    public Collection<MediaType> getMediaTypes() {
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(PROTOBUFFER_ENCODED_TYPE);
        return mediaTypes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T decode(Argument<T> type, InputStream inputStream) throws CodecException {
        try {
            Class<? extends Message> clazz = (Class<? extends Message>) type.getType();
            Message.Builder builder = getMessageBuilder(clazz);
            if (type.hasTypeVariables()) {
                throw new IllegalStateException("Not implemented yet!");
            } else {
                builder.mergeFrom(inputStream, this.extensionRegistry);
                return type.getType().cast(builder.build());
            }
        } catch (Exception e) {
            throw new CodecException("Error decoding Protobuff stream for type [" + type.getName() + "]: " + e.getMessage());
        }
    }

    @Override
    public <T> void encode(T object, OutputStream outputStream) throws CodecException {
        try {
            if (object instanceof Message) {
                ((Message) object).writeTo(outputStream);
            }
        } catch (IOException e) {
            throw new CodecException("Error encoding object [" + object + "] to OutputStream:" + e.getMessage());
        }
    }

    @Override
    public <T> byte[] encode(T object) throws CodecException {
        if (object instanceof Message) {
            return ((Message) object).toByteArray();
        }
        return new byte[0];
    }

    @Override
    public <T> ByteBuffer encode(T object, ByteBufferFactory allocator) throws CodecException {
        return allocator.copiedBuffer(encode(object));
    }

    /**
     * Create a new {@code Message.Builder} instance for the given class.
     * <p>This method uses a ConcurrentHashMap for caching method lookups.
     */
    private static Message.Builder getMessageBuilder(Class<? extends Message> clazz) throws Exception {
        Method method = methodCache.get(clazz);
        if (method == null) {
            method = clazz.getMethod("newBuilder");
            methodCache.put(clazz, method);
        }
        return (Message.Builder) method.invoke(clazz);
    }
}
