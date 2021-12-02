package com.example.demo;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class DemoApplicationTests {
	@Autowired
	MockMvc mvc;

	@Test
	@WithMockUser
	void requestWhenSessionPreviouslyInvalidatedThenIgnores() throws Exception {
		MockHttpSession session = new MockHttpSession() {
			@Override
			public void invalidate() {
				throw new IllegalStateException("already invalidated");
			}
		};
		this.mvc.perform(get("/").session(session)).andExpect(status().isNotFound());
	}

}
