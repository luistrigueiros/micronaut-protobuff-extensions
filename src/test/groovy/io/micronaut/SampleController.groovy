package io.micronaut

import com.example.wire.Example
import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.codec.ProtobufferCodec


@Controller
@CompileStatic
class SampleController {
    @Get(value = "/city", produces = ProtobufferCodec.PROTOBUFFER_ENCODED)
    @Consumes(ProtobufferCodec.PROTOBUFFER_ENCODED)
    Example.GeoPoint city() {
        Constant.DUBLIN
    }

    @Post(value = "/nearby", produces = ProtobufferCodec.PROTOBUFFER_ENCODED)
    @Consumes(ProtobufferCodec.PROTOBUFFER_ENCODED)
    Example.GeoPoint suggestVisitNearBy(Example.GeoPoint point) {
        Constant.DUBLIN
    }

}
