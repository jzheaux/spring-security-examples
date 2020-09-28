package io.jzheaux.springsecurity.twologinpages;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Order(1)
@Configuration
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/admin/**")
				.authorizeRequests((authz) -> authz
					.anyRequest().authenticated()
				)
				.formLogin((form) -> form
					.loginPage("/admin/login").permitAll()
				)
				.logout((logout) -> logout
					.logoutUrl("/admin/logout")
				);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		UserDetailsService adminUsers = new InMemoryUserDetailsManager(
				User.withUsername("admin")
					.password("{bcrypt}$2a$10$1/JJ4w5QOt4ln9ris9ERneYh1tXCuKedk/fjStcJlWGZvTDAha5AG") // "password"
					.roles("ADMIN", "USER")
					.build());
		auth.userDetailsService(adminUsers);
	}
}
