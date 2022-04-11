package com.example.messagingstompwebsocket;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.messaging.AuthorizationManagerMessageMatcherRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class MessagingStompWebsocketApplication {

	@Bean
	SecurityFilterChain web(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize.anyRequest().hasAuthority("view"))
			.formLogin(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	AuthorizationManager<Message<?>> messageAuthorizationManager(AuthorizationManagerMessageMatcherRegistry messages) {
		messages
			.simpMessageDestMatchers("/app/hello").hasAuthority("send")
			.anyMessage().hasAuthority("view");

		return messages.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager(
				User.withDefaultPasswordEncoder()
						.username("sender")
						.password("password")
						.authorities("view", "send")
						.build(),
				User.withDefaultPasswordEncoder()
						.username("viewer")
						.password("password")
						.authorities("view")
						.build()
		);
	}

	public static void main(String[] args) {
		SpringApplication.run(MessagingStompWebsocketApplication.class, args);
	}
}
