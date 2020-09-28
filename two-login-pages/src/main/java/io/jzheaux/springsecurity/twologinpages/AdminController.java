package io.jzheaux.springsecurity.twologinpages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@GetMapping("/login")
	public String loginPage() {
		return "admin/login";
	}

	@GetMapping
	public String home() {
		return "admin/home";
	}
}
