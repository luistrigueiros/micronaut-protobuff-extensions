package io.micronaut

import com.example.wire.Example
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class SimpleHttpGetSpec extends Specification implements TestUtiTrait {
    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Ignore
    @Unroll
    void "sample city should be dublin/using sample controller"(String url) {
        when:'The message is requested from the sever=[#url]'
            def response = getMessage(url, Example.GeoPoint.class)
        and:'The message is parser'
            Example.GeoPoint city  = Example.GeoPoint.parseFrom(response)
        then:'Should be Dublin'
            Constant.DUBLIN == city
        where:
            url = embeddedServer.getURL().toString() + '/city'
    }

    @Unroll
    void "sample city should be dublin/using programatic controller controller"(String url) {
        when:'The message is requested from the sever=[#url]'
            def response = getMessage(url, Example.GeoPoint.class)
        and:'The message is parser'
            Example.GeoPoint city  = Example.GeoPoint.parseFrom(response)
        then:'Should be Dublin'
            Constant.DUBLIN == city
        where:
            url = embeddedServer.getURL().toString() + '/town'
    }
}
