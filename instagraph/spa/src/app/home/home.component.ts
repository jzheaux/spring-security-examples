import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { PostService } from '../post/post.service';
import { PostLinks } from '../post/post';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  template: `
  <section>
    <form>
      <input type="text" placeholder="What's on your mind?" #post>
      <button class="primary" type="button" (click)="addPost(post.value)">Post</button>
    </form>
  </section>
  <section class="results">
  <section class="result" *ngFor="let entry of posts | keyvalue">
      <section>
        <p class="name"><a [routerLink]="['/person', entry.value.post.person]">{{entry.value.post.name}}</a></p>
        <p class="content">{{entry.value.post.content}}</p>
      </section>
      <section class="actions">
        <form *ngIf="entry.value.links['put'] && isUpdating">
          <textarea #update></textarea>
          <button class="primary" type="button" (click)="updatePost(entry.value, update.value)">Save</button>
          <button class="primary" type="button" (click)="isUpdating = false">Cancel</button>
        </form>
        <button *ngIf="entry.value.links['put'] && !isUpdating" (click)="isUpdating = true">Edit</button>
        <button *ngIf="entry.value.links['delete'] && !isUpdating" (click)="removePost(entry.value)">Remove</button>
      </section>
    </section>
  </section>
  `,
  styleUrl: './home.component.css'
})
export class HomeComponent {
  router = inject(Router)
  postService = inject(PostService)
  posts = new Map();
  isUpdating = false

  constructor() {
    console.log("Constructing");
    this.postService.getPosts()
      .then((posts) => { 
        for (var post of posts) {
          this.posts.set(post.post.id, post);
        }
      })
      .catch((error) => {
        if (error == 401) {
          this.router.navigateByUrl("/login");
        }
      });
  }

  addPost(text: string) {
    this.postService.addPost(text)
      .then((post) => this.posts.set(post.post.id, post))
      .catch((error) => {
        if (error == 401) {
          this.router.navigateByUrl("/login");
        }
      });
  }

  updatePost(post: PostLinks, text: string) {
    this.postService.updatePost(post, text)
      .then((updated) => {
        this.posts.set(post.post.id, updated)
        this.isUpdating = false
      })
      .catch((error) => {
        if (error == 401) {
          this.router.navigateByUrl("/login");
        }
      })
  }

  removePost(post: PostLinks) {
    this.postService.deletePost(post)
      .then(() => this.posts.delete(post.post.id))
      .catch((error) => {
        if (error == 401) {
          this.router.navigateByUrl("/login");
        }
      });
  }
}
