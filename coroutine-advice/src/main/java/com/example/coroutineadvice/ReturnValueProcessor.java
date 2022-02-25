package com.example.coroutineadvice;

import reactor.core.publisher.Mono;

public class ReturnValueProcessor {
    public Mono<String> after(Object result) {
        if ("hello".equals(result)) {
            return Mono.just("allowed");
        }
        return Mono.error(() -> new IllegalArgumentException("denied"));
    }
}
