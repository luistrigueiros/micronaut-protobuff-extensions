package com.example

import com.example.wire.Example
import com.google.protobuf.Message
import io.micronaut.context.ApplicationContext
import io.micronaut.http.codec.ProtobufferCodec
import io.micronaut.runtime.server.EmbeddedServer
import org.apache.hc.client5.http.fluent.Request
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class SimpleHttpPostSpec extends Specification {
    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    void "near by Dublin should be Dublin"() {
        when:
            Example.GeoPoint city = Example.GeoPoint.parseFrom(post(Constant.DUBLIN))
        then:
            Constant.DUBLIN == city
    }

    private byte[] post(Message message) {
        return Request.Post(embeddedServer.getURL().toString() + '/nearby')
                .addHeader("Content-Type", ProtobufferCodec.PROTOBUFFER_ENCODED)
                .bodyByteArray(message.toByteArray())
                .execute()
                .returnContent()
                .asBytes()
    }
}
