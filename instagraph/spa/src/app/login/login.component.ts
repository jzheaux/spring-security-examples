import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthInterceptor } from '../authinterceptor';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="login">
      <h2 class="section-heading">Login</h2>
      <form [formGroup]="credentials" (submit)="login()">
        <label for="email">Email</label>
        <input id="email" type="text" formControlName="email">

        <label for="password">Password</label>
        <input id="password" type="text" formControlName="password">

        <button type="submit" class="primary">Login</button>
      </form>
    </section>
  `,
  styleUrl: './login.component.css'
})
export class LoginComponent {
  credentials = new FormGroup({
    email: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });
  interceptor = inject(AuthInterceptor)
  router = inject(Router)

  constructor() {
    console.log("Login constructor");
  }

  login() {
    const email = this.credentials.value.email!;
    const password = this.credentials.value.password!;
    const encoded = btoa(email + ':' + password);
    const headers = { "Authorization": `Basic ${encoded}` };
    fetch("http://localhost:8080/token", { headers })
      .then((response) => response.text())
      .then((token) => {
        this.interceptor.token = token;
        this.router.navigateByUrl('/');
      });
  }
}
