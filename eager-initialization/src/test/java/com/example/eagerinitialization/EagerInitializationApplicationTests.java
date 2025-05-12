package com.example.eagerinitialization;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class EagerInitializationApplicationTests {

	@Autowired
	EagerInitializationApplication.MyService myService;

	@Test
	void preFilterWorks() {
		List<String> names = new ArrayList<>(List.of("bill", "rathers"));
		EagerInitializationApplication.MyObject object = this.myService.getMyObject();
		assertThat(object.getNames(names)).hasSize(1);
	}

	@Test
	void postFilterWorks() {
		List<String> names = new ArrayList<>(List.of("bill", "rathers"));
		EagerInitializationApplication.MyObject object = this.myService.getMyObject();
		assertThat(object.getOtherNames(names)).hasSize(1);
	}

	@Test
	void preAuthorizeWorks() {
		EagerInitializationApplication.MyObject myObject = this.myService.getMyObject();
		assertThatExceptionOfType(AuthorizationDeniedException.class).isThrownBy(myObject::denied);
	}

	@Test
	void postAuthorizeWorks() {
		EagerInitializationApplication.MyObject myObject = this.myService.getMyObject();
		assertThatExceptionOfType(AuthorizationDeniedException.class).isThrownBy(myObject::alsoDenied);
	}
}
