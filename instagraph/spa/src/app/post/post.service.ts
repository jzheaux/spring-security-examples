import { Injectable, inject } from '@angular/core';
import { PostLinks } from './post';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  readonly url = 'http://localhost:8080/posts';
  auth = inject(AuthService)
  
  async getPosts(): Promise<PostLinks[]> {
    const data = await this.auth.fetch(this.url);
    return await data.json() ?? [];
  }

  async getPostById(id: Number): Promise<PostLinks | undefined> {
    const data = await this.auth.fetch(`${this.url}/${id}`);
    return await data.json();
  }

  async addPost(body: string): Promise<PostLinks> {
    const options = {
      method: 'POST',
      headers: { 'Content-Type': 'text/plain' },
      body
    }
    const data = await this.auth.fetch(this.url, options);
    return await data.json();
  }

  async updatePost(post: PostLinks, body: string): Promise<PostLinks | undefined> {
    const options = {
      method: 'PUT',
      headers: { 'Content-Type': 'text/plain' },
      body
    }
    const data = await this.auth.fetch(post.links["put"], options);
    return await data.json();
  }

  async deletePost(post: PostLinks) {
    const options = {
      method: 'DELETE'
    }
    return await this.auth.fetch(post.links["delete"] as string, options);
  }

}
