package io.micronaut

import com.example.wire.Example
import spock.lang.Unroll

class ProgramacticControllerSpec extends BaseSpec {

    String url = embeddedServer.getURL().toString() + '/town'

    @Unroll
    void "sample city should be dublin/using programatic controller controller"() {
        when:'The message is requested from the sever=[#url]'
            def response = getMessage(url, Example.GeoPoint.class)
        and:'The message is parser'
            Example.GeoPoint city  = Example.GeoPoint.parseFrom(response)
        then:'Should be Dublin'
            SampleController.DUBLIN == city
    }
}
