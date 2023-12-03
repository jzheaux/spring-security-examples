package io.jzheaux.pluralsight.instagraph.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

public class Person {
	@Id
	Long id;

	String name;

	String email;

	Set<Post> posts;

	@Transient
	Set<String> authorities = new HashSet<>();

	public Person(String name, String email, Set<Post> posts) {
		this.name = name;
		this.email = email;
		this.posts = posts;
	}

	public Person withAuthorities(Set<String> authorities) {
		Person copy = new Person(this.name, this.email, this.posts);
		copy.id = this.id;
		copy.authorities = authorities;
		return copy;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public Set<Post> getPosts() {
		return Collections.unmodifiableSet(posts);
	}

	public boolean hasAuthority(String authority) {
		return this.authorities.contains(authority);
	}

	public boolean owns(Post post) {
		return post != null && this.id.equals(post.person());
	}
}
