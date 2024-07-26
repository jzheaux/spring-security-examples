package com.example.autoproxy;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;

@SpringBootApplication
@MyProxyTargetClassAnnotation
@Import(AutoProxyRegistrar.class)
public class AutoProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoProxyApplication.class, args);
	}

	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean
	Advisor advice() {
		MethodBeforeAdviceInterceptor interceptor = new MethodBeforeAdviceInterceptor(
			(method, args, target) -> System.out.println("before")
		);
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
		advisor.setPointcut(new AnnotationMatchingPointcut(null, MyAnnotation.class, true));
		advisor.setAdvice(interceptor);
		return advisor;
	}

	@Bean
	CommandLineRunner run(My my) {
		return new UseAdvice(my);
	}

	static final class UseAdvice implements CommandLineRunner {
		private final My my;

		UseAdvice(My my) {
			this.my = my;
		}

		@Override
		public void run(String... args) throws Exception {
			this.my.my();
		}
	}
}
