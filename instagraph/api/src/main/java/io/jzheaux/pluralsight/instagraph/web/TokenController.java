package io.jzheaux.pluralsight.instagraph.web;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
	private final JwtEncoder jwtEncoder;

	public TokenController(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	@GetMapping("/token")
	public String token(Authentication authentication) {
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(360000))
				.subject(authentication.getName())
				.audience(Arrays.asList("instagraph-client"))
				.claim("scp", AuthorityUtils.authorityListToSet(authentication.getAuthorities())).build();
		return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}
