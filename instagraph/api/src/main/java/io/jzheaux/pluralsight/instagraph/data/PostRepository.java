package io.jzheaux.pluralsight.instagraph.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
	@Override
	@PreAuthorize("hasAuthority('post:read')")
	Optional<Post> findById(Long id);

	@Override
	@PreAuthorize("hasAuthority('post:read')")
	Iterable<Post> findAll();

	@PreAuthorize("hasAuthority('post:write')")
	default Post create(Post post) {
		return save(post);
	}

	@PreAuthorize("hasAuthority('post:write') && authentication.principal.owns(#post)")
	default Post update(Post post) {
		return save(post);
	}

	@PreAuthorize("hasAuthority('post:write') && authentication.principal.owns(#post)")
	default void delete(Post post) {
		deleteById(post.id());
	}
}
