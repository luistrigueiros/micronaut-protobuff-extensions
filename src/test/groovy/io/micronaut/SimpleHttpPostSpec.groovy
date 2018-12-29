package io.micronaut

import com.example.wire.Example
import com.google.protobuf.Message
import spock.lang.Unroll

class SimpleHttpPostSpec extends BaseSpec {

    @Unroll
    void "near by Dublin should be Dublin"(String url) {
        setup:
            Message message = SampleController.DUBLIN
        when:'The message is posted to the server=[#url]'
            def response = postMessage(url, message)
        and:'The message is parsed'
            Example.GeoPoint city = Example.GeoPoint.parseFrom(response)
        then:'Should be Dublin'
        SampleController.DUBLIN == city
        where:
            url = embeddedServer.getURL().toString() + '/nearby'
    }
}
