package io.micronaut

import com.example.wire.Example
import io.micronaut.Constant
import io.micronaut.context.ApplicationContext
import io.micronaut.http.codec.ProtobufferCodec
import io.micronaut.runtime.server.EmbeddedServer
import org.apache.hc.client5.http.fluent.Request
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class SimpleHttpGetSpec extends Specification {
    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    void "sample city should be dublin"() {
        when:
            Example.GeoPoint city  = Example.GeoPoint.parseFrom(get())
        then:
        Constant.DUBLIN == city
    }

    private byte[] get() {
        Request.Get(embeddedServer.getURL().toString() + '/city')
                .addHeader("Content-Type", ProtobufferCodec.PROTOBUFFER_ENCODED)
                .execute()
                .returnContent()
                .asBytes()
    }
}
