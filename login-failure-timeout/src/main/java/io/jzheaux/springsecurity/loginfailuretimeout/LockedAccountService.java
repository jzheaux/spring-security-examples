package io.jzheaux.springsecurity.loginfailuretimeout;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class LockedAccountService {
	// replace with Guava cache with a timeout
	// or with a custom value that remembers additional lockout details
	private Map<String, AtomicInteger> attempts = new ConcurrentHashMap<>();

	public Integer getFailedAttempts(String username) {
		return this.attempts.getOrDefault(username, new AtomicInteger(0)).get();
	}

	public Integer incrementFailedAttempts(String username) {
		return this.attempts.computeIfAbsent(username, (k) -> new AtomicInteger(0)).incrementAndGet();
	}

	public void resetFailedAttempts(String username) {
		this.attempts.remove(username);
	}
}
