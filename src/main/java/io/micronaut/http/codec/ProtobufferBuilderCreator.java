package io.micronaut.http.codec;

import com.google.protobuf.Message;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ProtobufferBuilderCreator {
    private static final ConcurrentHashMap<Class<?>, Method> methodCache = new ConcurrentHashMap<>();

    /**
     * Create a new {@code Message.Builder} instance for the given class.
     * <p>This method uses a ConcurrentHashMap for caching method lookups.
     */
    public static Optional<Message.Builder> getMessageBuilder(Class<? extends Message> clazz)  {
        Message.Builder builder;
        try {
            Method method = methodCache.get(clazz);
            if (method == null) {
                method = clazz.getMethod("newBuilder");
                methodCache.put(clazz, method);
            }
            builder = (Message.Builder) method.invoke(clazz);
        }catch (Throwable throwable) {
            return Optional.empty();
        }
        return Optional.ofNullable(builder);
    }

}
