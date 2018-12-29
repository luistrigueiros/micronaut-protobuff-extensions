package io.micronaut

import com.google.protobuf.Message
import io.micronaut.context.ApplicationContext
import io.micronaut.http.codec.ProtobufferCodec
import io.micronaut.runtime.server.EmbeddedServer
import org.apache.hc.client5.http.fluent.Request
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class BaseSpec extends Specification {
    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)



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
