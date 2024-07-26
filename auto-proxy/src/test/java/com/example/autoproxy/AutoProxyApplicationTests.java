package com.example.autoproxy;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AutoProxyApplicationTests {

	@Autowired
	My my;

	@Test
	void contextLoads() {
		assertThat(this.my.my("works")).isEqualTo("before works");
	}

}
