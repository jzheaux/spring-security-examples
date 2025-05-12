package example.configurerdefaults;

import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

public class MyLogoutDsl extends AbstractHttpConfigurer<MyLogoutDsl, HttpSecurity> {
	LogoutConfigurer<?> logout;

	MyLogoutDsl(HttpSecurity http) throws Exception {
		// so that the configuration is applied immediately
		http.logout((logout) -> logout.logoutSuccessHandler(
			new HttpStatusReturningLogoutSuccessHandler(HttpStatus.I_AM_A_TEAPOT)));
		this.logout = http.getConfigurer(LogoutConfigurer.class);
	}

	MyLogoutDsl status(HttpStatus status) {
		this.logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(status));
		return this;
	}

}
