package io.jzheaux.springsecurity.loginfailuretimeout;

import org.springframework.security.core.AuthenticationException;

public class LockedAccountException extends AuthenticationException {
	private final Integer attempts;

	public LockedAccountException(String msg, Integer attempts) {
		super(msg);
		this.attempts = attempts;
	}

	public LockedAccountException(String msg, Integer attempts, Throwable cause) {
		super(msg, cause);
		this.attempts = attempts;
	}

	public Integer getAttempts() {
		return this.attempts;
	}
}
