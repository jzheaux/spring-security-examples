package com.example.coroutineadvice;

import java.lang.reflect.Method;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.reflect.full.KCallables;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.reactive.AwaitKt;
import kotlinx.coroutines.reactive.ReactiveFlowKt;
import kotlinx.coroutines.reactor.FluxKt;
import kotlinx.coroutines.reactor.MonoKt;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.CoroutinesUtils;
import org.springframework.core.KotlinDetector;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.convert.converter.Converter;

public class TwoMethodInterceptor implements MethodInterceptor {
    private static final String COROUTINES_FLOW_CLASS_NAME = "kotlinx.coroutines.flow.Flow";

    private static final int RETURN_TYPE_METHOD_PARAMETER_INDEX = -1;

    private final Converter<Object, Mono<Object>> advice = (result) -> {
        if (result.toString().endsWith("o")) {
            return Mono.just("accepted");
        }
        return Mono.error(() -> new IllegalArgumentException("denied"));
    };

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Two Method Interceptor");
        Method method = invocation.getMethod();
        Class<?> returnType = method.getReturnType();
        boolean isSuspendingFunction = KotlinDetector.isSuspendingFunction(method);
        boolean hasFlowReturnType = COROUTINES_FLOW_CLASS_NAME
                .equals(new MethodParameter(method, RETURN_TYPE_METHOD_PARAMETER_INDEX).getParameterType().getName());
        if (Mono.class.isAssignableFrom(returnType)) {
            Object result = invocation.proceed();
            return ((Mono<?>) result).flatMap((r) -> this.advice.convert(r)).then((Mono<?>) result);
        }
        if (Flux.class.isAssignableFrom(returnType)) {
            Object result = invocation.proceed();
            return ((Flux<?>) result).flatMap((r) -> this.advice.convert(r)).thenMany((Flux<?>) result);
        }
        if (!isSuspendingFunction && hasFlowReturnType) {
            ReactiveAdapter adapter = ReactiveAdapterRegistry.getSharedInstance().getAdapter(returnType);
            Flux<?> flux = Flux.from(adapter.toPublisher(invocation.proceed()));
            return ReactiveFlowKt.asFlow(flux.flatMap((r) -> this.advice.convert(r)).thenMany(flux));
        }
        if (isSuspendingFunction) {
            Mono<?> response = Mono.from(CoroutinesUtils.invokeSuspendingFunction(invocation.getMethod(), invocation.getThis(),
                            invocation.getArguments()))
                    .flatMap((r) -> this.advice.convert(r).then(Mono.just(r)));
            return AwaitKt.awaitSingleOrNull(response,
                    (Continuation<Object>) invocation.getArguments()[invocation.getArguments().length - 1]);
        }
        return invocation.proceed();
    }
}
