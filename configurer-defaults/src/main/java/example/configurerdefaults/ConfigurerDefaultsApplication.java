package example.configurerdefaults;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@SpringBootApplication
public class ConfigurerDefaultsApplication {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.with(new MyLogoutDsl(http), withDefaults())
			.logout((logout) -> logout.logoutSuccessHandler(
				new SimpleUrlLogoutSuccessHandler())); // takes precedence since second
		return http.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(ConfigurerDefaultsApplication.class, args);
	}

}
