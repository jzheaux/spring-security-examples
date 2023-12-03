package io.jzheaux.pluralsight.instagraph.security;

import java.util.Collection;

import io.jzheaux.pluralsight.instagraph.data.Person;
import io.jzheaux.pluralsight.instagraph.data.PersonRepository;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class PersonAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {
	private final JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();

	private final PersonRepository persons;

	public PersonAuthenticationConverter(PersonRepository persons) {
		this.persons = persons;
		this.converter.setAuthorityPrefix("");
	}

	@Override
	public JwtAuthenticationToken convert(Jwt jwt) {
		return this.persons.findByEmail(jwt.getSubject())
			.map((person) -> new PersonAuthenticationToken(jwt, person, this.converter.convert(jwt)))
			.orElseThrow(() -> new JwtException("user not found"));
	}

	private static class PersonAuthenticationToken extends JwtAuthenticationToken {
		private Person person;

		public PersonAuthenticationToken(Jwt jwt, Person person, Collection<GrantedAuthority> authorities) {
			super(jwt, authorities, person.getEmail());
			this.person = person.withAuthorities(AuthorityUtils.authorityListToSet(authorities));
		}

		@Override
		public Object getPrincipal() {
			return this.person;
		}
	}

}
