package com.example

import com.example.wire.Example
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.codec.ProtobufferCodec
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class SampleControllerSpec extends Specification {
    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Shared
    @AutoCleanup
    RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

    void "sample city should be dublin"() {
        when:
            HttpRequest request = HttpRequest.GET('/city')
            Example.GeoPoint city  = client.toBlocking().retrieve(request, Example.GeoPoint)
        then:
            Constant.DUBLIN == city
    }


    void  "near by Dublin should be Dublin"() {
        when:
            HttpRequest request = HttpRequest.POST('/nearby', Constant.DUBLIN)
                    .contentType(ProtobufferCodec.PROTOBUFFER_ENCODED_TYPE)
            Example.GeoPoint city  = client.toBlocking().retrieve(request, Example.GeoPoint)
        then:
            Constant.DUBLIN == city
    }
}
