package io.jzheaux.pluralsight.instagraph.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
@PreAuthorize("denyAll")
public interface PersonRepository extends CrudRepository<Person, Long> {
	@PreAuthorize("permitAll")
	Optional<Person> findByEmail(String email);
}
