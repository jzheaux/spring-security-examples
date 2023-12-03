package io.jzheaux.pluralsight.instagraph.web;

import java.util.function.Supplier;

import io.jzheaux.pluralsight.instagraph.data.Person;
import io.jzheaux.pluralsight.instagraph.data.Post;
import io.jzheaux.pluralsight.instagraph.data.PostRepository;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/posts")
public class PostController {
	private final Supplier<Person> currentPerson;
	private final PostRepository posts;

	public PostController(Supplier<Person> currentPerson, PostRepository posts) {
		this.currentPerson = currentPerson;
		this.posts = posts;
	}

	@GetMapping
	public Iterable<Post> getPosts() {
		return this.posts.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Post>> getPost(@PathVariable("id") Long id) {
		return this.posts.findById(id)
			.map(this::toModel)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<EntityModel<Post>> addPost(@RequestBody String content) {
		Person person = this.currentPerson.get();
		Post post = this.posts.create(new Post(null, content, person.getId()));
		EntityModel<Post> model = toModel(post);
		return ResponseEntity.created(model.getRequiredLink("get").toUri()).body(model);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Post>> updatePost(@PathVariable("id") Long id, @RequestBody String content) {
		return this.posts.findById(id)
			.map((post) -> this.posts.update(new Post(post.id(), content, post.person())))
			.map(this::toModel)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
		this.posts.findById(id).ifPresent(this.posts::delete);
		return ResponseEntity.noContent().build();
	}

	private EntityModel<Post> toModel(Post post) {
		Person person = this.currentPerson.get();
		PostController linker = methodOn(getClass());
		return EntityModel.of(post)
				.addIf(person.hasAuthority("post:read"), () -> linkTo(linker.getPost(post.id())).withRel("get"))
				.addIf(person.hasAuthority("post:write") && person.owns(post), () -> linkTo(linker.updatePost(post.id(), null)).withRel("put"))
				.addIf(person.hasAuthority("post:write") && person.owns(post), () -> linkTo(linker.deletePost(post.id())).withRel("delete"));
	}
}
