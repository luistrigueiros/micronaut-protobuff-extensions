package io.micronaut

import com.example.wire.Example
import com.google.protobuf.Message
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class SimpleHttpPostSpec extends Specification implements TestUtiTrait {
    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Unroll
    void "near by Dublin should be Dublin"(String url) {
        setup:
            Message message = Constant.DUBLIN
        when:'The message is posted to the server=[#url]'
            def response = postMessage(url, message)
        and:'The message is parsed'
            Example.GeoPoint city = Example.GeoPoint.parseFrom(response)
        then:'Should be Dublin'
            Constant.DUBLIN == city
        where:
            url = embeddedServer.getURL().toString() + '/nearby'
    }
}
