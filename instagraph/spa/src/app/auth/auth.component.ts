import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="login">
      <h2 class="section-heading">Login</h2>
      <span>{{error}}</span>
      <form [formGroup]="credentials" (submit)="login()">
        <label for="email">Email</label>
        <input id="email" type="text" formControlName="email">

        <label for="password">Password</label>
        <input id="password" type="password" formControlName="password">

        <button type="submit" class="primary">Login</button>
      </form>
    </section>
  `,
  styleUrl: './auth.component.css'
})
export class LoginComponent {
  credentials = new FormGroup({
    email: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });
  error = ''
  auth = inject(AuthService)
  router = inject(Router)

  login() {
    const email = this.credentials.value.email!;
    const password = this.credentials.value.password!;
    this.auth.fetchToken(email, password)
      .catch((error) => this.error = error)
      .then((token) => this.router.navigateByUrl('/'));
  }
}
