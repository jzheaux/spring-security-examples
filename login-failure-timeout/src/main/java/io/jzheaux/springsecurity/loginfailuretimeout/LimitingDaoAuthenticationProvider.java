package io.jzheaux.springsecurity.loginfailuretimeout;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class LimitingDaoAuthenticationProvider extends DaoAuthenticationProvider {
	private final LockedAccountService lockedAccounts;

	public LimitingDaoAuthenticationProvider(
			LockedAccountService lockedAccounts, UserDetailsService userDetailsService) {

		this.lockedAccounts = lockedAccounts;
		this.setUserDetailsService(userDetailsService);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Integer attempts = this.lockedAccounts.getFailedAttempts(authentication.getName());

		if (attempts > 5) {
			throw new LockedAccountException("Account locked", attempts);
		}

		try {
			Authentication result = super.authenticate(authentication);
			this.lockedAccounts.resetFailedAttempts(authentication.getName());
			return result;
		} catch (BadCredentialsException e) {
			attempts = this.lockedAccounts.incrementFailedAttempts(authentication.getName());
			if (attempts > 5) {
				throw new LockedAccountException("Account locked", attempts, e);
			} else {
				throw e;
			}
		}
	}
}
