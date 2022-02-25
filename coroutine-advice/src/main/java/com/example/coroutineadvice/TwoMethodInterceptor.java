package com.example.coroutineadvice;

import java.lang.reflect.Method;

import kotlinx.coroutines.reactive.ReactiveFlowKt;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.KotlinDetector;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;

public class TwoMethodInterceptor implements MethodInterceptor {
    private static final String COROUTINES_FLOW_CLASS_NAME = "kotlinx.coroutines.flow.Flow";

    private static final int RETURN_TYPE_METHOD_PARAMETER_INDEX = -1;

    private final ReturnValueProcessor after = new ReturnValueProcessor();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Two Method Interceptor");
        Method method = invocation.getMethod();
        Class<?> returnType = method.getReturnType();
        boolean isSuspendingFunction = KotlinDetector.isSuspendingFunction(method);
        boolean hasFlowReturnType = COROUTINES_FLOW_CLASS_NAME
                .equals(new MethodParameter(method, RETURN_TYPE_METHOD_PARAMETER_INDEX).getParameterType().getName());
        Object result = invocation.proceed();
        if (Mono.class.isAssignableFrom(returnType)) {
            return ((Mono<?>) result).flatMap((r) -> this.after.after(r)).then((Mono<?>) result);
        }
        if (Flux.class.isAssignableFrom(returnType)) {
            return ((Flux<?>) result).flatMap((r) -> this.after.after(r)).thenMany((Flux<?>) result);
        }
        if (!isSuspendingFunction && hasFlowReturnType) {
            ReactiveAdapter adapter = ReactiveAdapterRegistry.getSharedInstance().getAdapter(returnType);
            Flux<?> flux = Flux.from(adapter.toPublisher(result));
            return ReactiveFlowKt.asFlow(flux.flatMap((r) -> this.after.after(r)).thenMany(flux));
        }
        if (isSuspendingFunction) {
            // ???

            // result is of type CoroutineSingletons... want to somehow adapt into a Publisher
            // so calling ReturnValueProcessor is possible

            // NOTE: cannot pre-emptively call CoroutineUtils.invokeSuspendingFunction since that
            // is not aware of other method interceptors
        }
        return result;
    }
}
