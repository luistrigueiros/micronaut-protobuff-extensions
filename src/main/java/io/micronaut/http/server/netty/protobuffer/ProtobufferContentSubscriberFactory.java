package io.micronaut.http.server.netty.protobuffer;

import io.micronaut.core.annotation.Internal;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.codec.ProtobufferCodec;
import io.micronaut.http.server.netty.HttpContentProcessor;
import io.micronaut.http.server.netty.HttpContentSubscriberFactory;
import io.micronaut.http.server.netty.NettyHttpRequest;
import io.micronaut.http.server.netty.configuration.NettyHttpServerConfiguration;

import javax.inject.Singleton;

@Consumes({ProtobufferCodec.PROTOBUFFER_ENCODED})
@Singleton
@Internal
public class ProtobufferContentSubscriberFactory implements HttpContentSubscriberFactory {

    private final NettyHttpServerConfiguration configuration;

    public ProtobufferContentSubscriberFactory(NettyHttpServerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public HttpContentProcessor build(NettyHttpRequest request) {
        return new ProtobufferContentProcessor(request, configuration);
    }
}
