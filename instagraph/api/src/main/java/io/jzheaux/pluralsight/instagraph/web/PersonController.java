package io.jzheaux.pluralsight.instagraph.web;

import io.jzheaux.pluralsight.instagraph.data.Person;
import io.jzheaux.pluralsight.instagraph.data.PersonRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {
	private final PersonRepository persons;

	public PersonController(PersonRepository persons) {
		this.persons = persons;
	}

	@GetMapping("/me")
	public Person person(@AuthenticationPrincipal Person person) {
		return person;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Person> person(@PathVariable("id") Long id) {
		return this.persons.findById(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}
}
