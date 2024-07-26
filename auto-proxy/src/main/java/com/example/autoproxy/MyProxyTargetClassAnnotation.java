package com.example.autoproxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.AdviceMode;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyProxyTargetClassAnnotation {
	AdviceMode mode() default AdviceMode.PROXY;
	boolean proxyTargetClass() default false;
}
