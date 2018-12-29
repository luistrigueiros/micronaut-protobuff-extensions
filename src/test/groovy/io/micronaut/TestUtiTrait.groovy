package io.micronaut

import com.google.protobuf.Message
import groovy.transform.CompileStatic
import io.micronaut.http.codec.ProtobufferCodec
import org.apache.hc.client5.http.fluent.Request

@CompileStatic
trait TestUtiTrait {
    byte[] getMessage(String url, Class aClass) {
        Request.Get(url)
                .addHeader("Content-Type", ProtobufferCodec.PROTOBUFFER_ENCODED)
                .addHeader(ProtobufferCodec.X_PROTOBUF_MESSAGE_HEADER, aClass.name)
                .execute()
                .returnContent()
                .asBytes()
    }

    byte[] postMessage(String url, Message message) {
        return Request.Post(url)
                .addHeader("Content-Type", ProtobufferCodec.PROTOBUFFER_ENCODED)
                .addHeader(ProtobufferCodec.X_PROTOBUF_MESSAGE_HEADER, message.class.name)
                .bodyByteArray(message.toByteArray())
                .execute()
                .returnContent()
                .asBytes()
    }
}
