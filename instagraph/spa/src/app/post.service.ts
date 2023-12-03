import { Injectable } from '@angular/core';
import { Post } from './post';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  readonly url = 'http://localhost:8080/posts';

  constructor() { }
  
  async getPosts(): Promise<Post[]> {
    const data = await fetch(this.url);
    if (data.status == 401) {
      return Promise.reject(data.status);
    }
    return await data.json() ?? [];
  }

  async getPostById(id: Number): Promise<Post | undefined> {
    const data = await fetch(`${this.url}/${id}`);
    if (data.status == 401) {
      return Promise.reject(data.status);
    }
    return await data.json();
  }

  async addPost(body: any): Promise<Post> {
    const options = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    }
    const data = await fetch(`${this.url}`, options);
    if (data.status == 401) {
      return Promise.reject(data.status);
    }
    return await data.json();
  }

  async updatePost(id: Number, body: String): Promise<Post | undefined> {
    const options = {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    }
    const data = await fetch(`${this.url}/${id}`, options);
    if (data.status == 401) {
      return Promise.reject(data.status);
    }
    return await data.json();
  }

  async deletePost(id: Number) {
    const options = {
      method: 'DELETE'
    }
    const data = await fetch(`${this.url}/${id}`, options);
    if (data.status == 401) {
      return Promise.reject(data.status);
    }
    return await data.json();
  }

}
