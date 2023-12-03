import { Injectable } from "@angular/core";

@Injectable({
	providedIn: 'root'
})
export class AuthInterceptor {
	token: String = '';
	csrfToken: String = '';

	constructor() {
		const { fetch: originalFetch } = window;
		window.fetch = async (...args) => {
			let [resource, options] = args;
			if (options && options.headers) {
				if (this.token) {
					(options.headers as Headers).set('Authorization', `Bearer ${this.token}`);
				}
			}
			const response = await originalFetch(resource, options);
			if (response.status == 401) {
				return Promise.reject();
			}
			return response;
		};
	}
}