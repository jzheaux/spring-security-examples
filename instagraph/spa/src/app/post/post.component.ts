import { Component, inject, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../post.service';
import { Post } from '../post';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule],
  template: `
    <p>
      {{post.content}}
    </p>
  `,
  styleUrl: './post.component.css'
})
export class PostComponent {
  @Input() post!: Post;
}
