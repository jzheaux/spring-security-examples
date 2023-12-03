import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { PostComponent } from './post/post.component';
import { LoginComponent } from './login/login.component';

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
		path: 'posts/:id',
		component: PostComponent,
		title: 'Post'
	}
];

export default routeConfig;