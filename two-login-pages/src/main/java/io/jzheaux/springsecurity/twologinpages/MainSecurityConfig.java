package io.jzheaux.springsecurity.twologinpages;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Order(2)
@Configuration
public class MainSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/**")
				.authorizeRequests((authz) -> authz
					.mvcMatchers("/favicon.ico", "/error").permitAll()
					.anyRequest().authenticated()
				)
				.formLogin((form) -> form
					.loginPage("/login").permitAll()
				);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		UserDetailsService adminUsers = new InMemoryUserDetailsManager(
				User.withUsername("user")
					.password("{bcrypt}$2a$10$1/JJ4w5QOt4ln9ris9ERneYh1tXCuKedk/fjStcJlWGZvTDAha5AG") // "password"
					.roles("USER")
					.build());
		auth.userDetailsService(adminUsers);
	}
}
