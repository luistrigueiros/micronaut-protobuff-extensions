package io.micronaut.protobuffer.convert;

import com.google.protobuf.Message;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import io.netty.buffer.ByteBuf;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ByteBufToMessageConverter implements TypeConverter<ByteBuf, Message> {
    @Override
    public Optional<Message> convert(ByteBuf object, Class<Message> targetType, ConversionContext context) {
        if (true) {
            throw new IllegalStateException("Convertion from ByteBuff to Message is not implemented yet");
        }

        return Optional.empty();
    }
}
