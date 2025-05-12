package com.example.eagerinitialization;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@SpringBootApplication//(exclude = AopAutoConfiguration.class) // uncomment to fix issue
@EnableMethodSecurity
public class EagerInitializationApplication {

	@Component
	public static class RemovesFirstInterceptor implements FactoryBean<Object> {
		@Override
		public Object getObject() throws Exception {
			return new Object();
		}

		@Override
		public Class<?> getObjectType() {
			return Object.class;
		}
	}

	/* Remove this line to cause two interceptors to be missing

	@Component
	public static class RemovesSecondInterceptor implements FactoryBean<Object> {
		@Override
		public Object getObject() throws Exception {
			return new Object();
		}

		@Override
		public Class<?> getObjectType() {
			return Object.class;
		}
	}

	/* Remove this line to cause three interceptors to be missing

	@Component
	public static class RemovesThirdInterceptor implements FactoryBean<Object> {
		@Override
		public Object getObject() throws Exception {
			return new Object();
		}

		@Override
		public Class<?> getObjectType() {
			return Object.class;
		}
	}

	/* Remove this line (and the end comment) to cause four interceptors to be missing

	@Component
	public static class RemovesFourthInterceptor implements FactoryBean<Object> {
		@Override
		public Object getObject() throws Exception {
			return new Object();
		}

		@Override
		public Class<?> getObjectType() {
			return Object.class;
		}
	}*/

	@Service
	public static class MyService {
		@AuthorizeReturnObject
		MyObject getMyObject() {
			return new MyObject();
		}
	}

	public static class MyObject {
		@PreFilter(value="filterObject.length() > 5", filterTarget = "names")
		public List<String> getNames(List<String> names) {
			return names;
		}

		@PreAuthorize("denyAll()")
		public String denied() {
			return "denied";
		}

		@PostAuthorize("denyAll()")
		public String alsoDenied() {
			return "also denied";
		}

		@PostFilter(value="filterObject.length() > 5")
		public List<String> getOtherNames(List<String> names) {
			return names;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(EagerInitializationApplication.class, args);
	}

}
