package io.jzheaux.pluralsight.instagraph.security;

import java.util.List;
import java.util.function.Supplier;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import io.jzheaux.pluralsight.instagraph.data.Person;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
		corsConfiguration.setExposedHeaders(List.of("X-CSRF-TOKEN"));
		return (request) -> corsConfiguration;
	}

	@Bean
	Supplier<Person> currentPerson() {
		return () -> (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Bean
	SecurityFilterChain apiEndpoints(HttpSecurity http, PersonAuthenticationConverter person) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
			.httpBasic(Customizer.withDefaults())
			.oauth2ResourceServer((oauth2) -> oauth2
				.jwt((jwt) -> jwt.jwtAuthenticationConverter(person))
			);
		return http.build();
	}

	@Bean
	public JwtDecoder jwtDecoder(RSAKey key) throws Exception {
		return NimbusJwtDecoder.withPublicKey(key.toRSAPublicKey()).build();
	}

	@Bean
	public JwtEncoder jwtEncoder(RSAKey key) throws Exception {
		return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(key)));
	}

	@Bean
	public RSAKey key() throws Exception {
		return new RSAKeyGenerator(2048).generate();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager(
			User.withDefaultPasswordEncoder()
				.username("candice@example.org")
				.password("password")
				.authorities("post:read", "post:write")
				.build()
		);
	}
}
