package io.jzheaux.pluralsight.instagraph.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
	Optional<Person> findByEmail(String email);
}
