package com.gkatzioura.reactor.fluxfiltercapture;

import java.nio.charset.StandardCharsets;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BodyCaptureResponse extends ServerHttpResponseDecorator {

    private final StringBuilder body = new StringBuilder();

    public BodyCaptureResponse(ServerHttpResponse delegate) {
        super(delegate);
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        Flux<DataBuffer> buffer = Flux.from(body);
        return super.writeWith(buffer.doOnNext(this::capture));
    }

    private void capture(DataBuffer buffer) {
        this.body.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString());
    }

    public String getFullBody() {
        return this.body.toString();
    }

}
