package com.example.demo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@SpringBootApplication
public class DemoApplication {

	@Bean
	SecurityFilterChain http(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
		http.addFilter(preAuthenticated());
		http.addFilterBefore(new QuietInvalidateHttpSessionFilter(), AbstractPreAuthenticatedProcessingFilter.class);
		return http.build();
	}

	AbstractPreAuthenticatedProcessingFilter preAuthenticated() {
		Authentication pre = new UsernamePasswordAuthenticationToken("hello", "world", AuthorityUtils.createAuthorityList("ROLE_USER"));
		AbstractPreAuthenticatedProcessingFilter filter = new AbstractPreAuthenticatedProcessingFilter() {
			@Override
			protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
				return pre.getPrincipal();
			}

			@Override
			protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
				return pre.getCredentials();
			}
		};
		filter.setCheckForPrincipalChanges(true);
		filter.setAuthenticationManager((authentication) -> pre);
		return filter;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
