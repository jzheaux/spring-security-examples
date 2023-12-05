import { Injectable } from "@angular/core";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	token: String = '';
	csrfToken: String | undefined = '';

	async fetch(resource: RequestInfo, options: RequestInit = {
		method: "GET",
		headers: { "Content-Type": "application/json" }
	}) {
		options.credentials = "include";
		if (options) {
			const copy = new Headers(options?.headers);
			if (this.token) {
				copy.set('Authorization', `Bearer ${this.token}`);
			}
			if (this.csrfToken) {
				copy.set('X-CSRF-TOKEN', this.csrfToken.toString());
			}
			options.headers = copy;
		}
		const response = await fetch(resource, options);
		this.csrfToken = response.headers.get('X-CSRF-TOKEN')?.toString();
		if (response.status == 401) {
			return Promise.reject(401);
		}
		return response;
	}

	fetchToken(email: string, password: string) {
		const encoded = btoa(email + ':' + password);
		const headers = { "Authorization": `Basic ${encoded}` };
		return fetch("http://localhost:8080/token", { headers, credentials : "include" })
			.then((response) => response.text())
			.then((token) => this.token = token);
	}
}