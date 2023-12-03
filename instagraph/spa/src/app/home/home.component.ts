import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PostService } from '../post.service';
import { Post } from '../post';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule
  ],
  template: `
  <section>
    <form>
      <input type="text" placeholder="What's on your mind?" #post>
      <button class="primary" type="button" (click)="addPost(post.value)">Post</button>
    </form>
  </section>
  <section class="results">
    <li *ngFor="let post of posts">
      Content: {{post.content}}
      <button (click)="removePost(post)">Remove</button>
    </li>
  </section>
`,
  styleUrl: './home.component.css'
})
export class HomeComponent {
  router = inject(Router)
  postService = inject(PostService)
  posts: Post[] = [];

  constructor() {
    console.log("Constructing");
    this.postService.getPosts()
      .then((posts) => { this.posts = posts })
      .catch((error) => {
        if (error == 401) {
          this.router.navigateByUrl("/login");
        }
      });
  }

  addPost(text: string) {
    this.postService.addPost(text)
      .then((post) => this.posts.push(post))
      .catch((error) => {
        if (error == 401) {
          this.router.navigateByUrl("/login");
        }
      });
  }

  removePost(post: Post) {
    this.postService.deletePost(post.id)
      .then(() => this.posts = this.posts.filter((candidate) => candidate.id != post.id))
      .catch((error) => {
        if (error == 401) {
          this.router.navigateByUrl("/login");
        }
      });
  }
}
