package com.example.coroutineadvice;

import java.lang.reflect.Method;

import org.springframework.aop.Advisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CoroutineAdviceApplication {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    Advisor oneMethodInterceptor() {
        StaticMethodMatcherPointcutAdvisor a = new StaticMethodMatcherPointcutAdvisor(new OneMethodInterceptor()) {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return method.getName().startsWith("hello");
            }
        };
        a.setOrder(0);
        return a;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    Advisor twoMethodInterceptor() {
        StaticMethodMatcherPointcutAdvisor a = new StaticMethodMatcherPointcutAdvisor(new TwoMethodInterceptor()) {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return method.getName().startsWith("hello");
            }
        };
        a.setOrder(1);
        return a;
    }

    public static void main(String[] args) {
        SpringApplication.run(CoroutineAdviceApplication.class, args);
    }

}
