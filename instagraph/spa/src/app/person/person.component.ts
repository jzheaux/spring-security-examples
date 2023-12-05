import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { PersonService } from './person.service';
import { Person } from './person';

@Component({
  selector: 'app-person',
  standalone: true,
  imports: [CommonModule],
  template: `
    <p *ngIf="person">
      {{person.name}} - {{person.email}}
    </p>
  `,
  styleUrl: './person.component.css'
})
export class PersonComponent {
  route: ActivatedRoute = inject(ActivatedRoute);
  router = inject(Router)
  personService = inject(PersonService)
  person: Person | undefined;

  constructor() {
    const id = parseInt(this.route.snapshot.params['id']);
    this.personService.getPersonById(id)
      .then((person) => this.person = person)
      .catch((error) => {
        if (error == 401) {
          this.router.navigateByUrl("/login");
        }
      });
  }
}
