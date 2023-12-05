import { Injectable, inject } from '@angular/core';
import { Person } from './person';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class PersonService {

  readonly url = 'http://localhost:8080/persons';
  auth = inject(AuthService)

  async getPersonById(id: Number): Promise<Person | undefined> {
    const data = await this.auth.fetch(`${this.url}/${id}`);
    return await data.json();
  }

}
