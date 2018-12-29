package io.micronaut

import com.example.wire.Example
import spock.lang.Unroll

class SimpleHttpGetSpec extends BaseSpec {
    @Unroll
    void "sample city should be dublin/using sample controller"(String url) {
        when:'The message is requested from the sever=[#url]'
            def response = getMessage(url, Example.GeoPoint.class)
        and:'The message is parser'
            Example.GeoPoint city  = Example.GeoPoint.parseFrom(response)
        then:'Should be Dublin'
        SampleController.DUBLIN == city
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
            SampleController.DUBLIN == city
        where:
            url = embeddedServer.getURL().toString() + '/town'
    }
}
