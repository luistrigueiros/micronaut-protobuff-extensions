package io.micronaut.protobuffer.convert;

import com.google.protobuf.Message;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import io.netty.buffer.ByteBuf;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class MessageToByteBufConverter implements TypeConverter<Message, ByteBuf> {
    @Override
    public Optional<ByteBuf> convert(Message object, Class<ByteBuf> targetType, ConversionContext context) {
        if (true) {
            throw new IllegalStateException("Convertion from Message to ByteBuff is not implemented yet");
        }
        return Optional.empty();
    }
}
