import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from '../app/login/login.component';
import { RegisterComponent } from './register/register.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  // {path: '', component: NavBarComponent},
  {path: 'main', component: NavBarComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
