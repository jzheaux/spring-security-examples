package io.jzheaux.springsecurity.twologinpages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping("/login")
	public String userLogin() {
		return "login";
	}

	@GetMapping
	public String home() {
		return "home";
	}
}
