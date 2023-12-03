package io.jzheaux.pluralsight.instagraph.web;

import io.jzheaux.pluralsight.instagraph.data.Person;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person")
public class PersonController {

	@GetMapping
	public Person person(@AuthenticationPrincipal Person person) {
		return person;
	}
}
