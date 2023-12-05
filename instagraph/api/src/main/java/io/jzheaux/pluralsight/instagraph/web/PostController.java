package io.jzheaux.pluralsight.instagraph.web;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.jzheaux.pluralsight.instagraph.data.Person;
import io.jzheaux.pluralsight.instagraph.data.PersonRepository;
import io.jzheaux.pluralsight.instagraph.data.Post;
import io.jzheaux.pluralsight.instagraph.data.PostRepository;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
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
	public Iterable<PostModel> getPosts() {
		return StreamSupport.stream(this.posts.findAll().spliterator(), false)
			.map(this::toModel).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostModel> getPost(@PathVariable("id") Long id) {
		return this.posts.findById(id)
			.map(this::toModel)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<PostModel> addPost(@RequestBody String content) {
		Person person = this.currentPerson.get();
		Post post = this.posts.create(new Post(null, content, person.getId(), person.getName()));
		PostModel model = toModel(post);
		return ResponseEntity.created(model.links().get("get")).body(model);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PostModel> updatePost(@PathVariable("id") Long id, @RequestBody String content) {
		return this.posts.findById(id)
			.map((post) -> this.posts.update(new Post(post.id(), content, post.person(), post.name())))
			.map(this::toModel)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
		this.posts.findById(id).ifPresent(this.posts::delete);
		return ResponseEntity.noContent().build();
	}

	private PostModel toModel(Post post) {
		Person person = this.currentPerson.get();
		PostController linker = methodOn(getClass());
		Map<String, URI> links = new HashMap<>();
		if (person.hasAuthority("post:read")) {
			links.put("get", linkTo(linker.getPost(post.id())).toUri());
		}
		if (person.hasAuthority("post:write") && person.owns(post)) {
			links.put("put", linkTo(linker.updatePost(post.id(), null)).toUri());
			links.put("delete", linkTo(linker.deletePost(post.id())).toUri());
		}
		return new PostModel(post, links);
	}

	public record PostModel(Post post, Map<String, URI> links) {
	}
}