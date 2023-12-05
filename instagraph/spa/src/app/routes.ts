import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './auth/auth.component';
import { PersonComponent } from './person/person.component';

const routeConfig: Routes = [
	{
		path: '',
		component: HomeComponent,
		title: 'Home page'
	},
	{
		path: 'login',
		component: LoginComponent,
		title: 'Login page'
	},
	{
		path: 'person/:id',
		component: PersonComponent,
		title: 'Profile page'
	}
];

export default routeConfig;