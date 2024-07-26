package com.example.autoproxy;

import org.springframework.stereotype.Component;

@Component
public final class MyImpl implements My {

	@Override
	public String my() {
		return "";
	}
}
