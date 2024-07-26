package com.example.autoproxy;

import org.springframework.context.annotation.AdviceMode;

public @interface MyProxyTargetClassAnnotation {
	AdviceMode mode() default AdviceMode.PROXY;
	boolean proxyTargetClass() default false;
}
