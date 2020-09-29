package io.jzheaux.springsecurity.loginfailuretimeout;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {
	@Bean
	UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager(User.withUsername("user")
				.password("{bcrypt}$2a$10$1/JJ4w5QOt4ln9ris9ERneYh1tXCuKedk/fjStcJlWGZvTDAha5AG") // "password"
				.roles("USER")
				.build());
	}

	// introduce a failure handler that modifies the error details depending on what's inside the custom exception
}
