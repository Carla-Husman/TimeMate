import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CalendarComponent } from './components/calendar/calendar.component';
import { BadgesComponent } from './components/badges/badges.component';
import { ProfileComponent } from './components/profile/profile.component';
import { RegisterComponent } from './components/register/register.component';
import { CreateTaskComponent } from './components/tasks/create-task/create-task.component';
import { EditTaskComponent } from './components/tasks/edit-task/edit-task.component';
import { AdminComponent } from './components/admin/admin.component';

const routes: Routes = [
  { path: '', redirectTo: 'account', pathMatch: 'full' },
  { path: 'calendar', component: CalendarComponent},
  { path: 'badges', component: BadgesComponent},
  { path: 'profile', component: ProfileComponent },
  { path: 'account', component: RegisterComponent },
  { path: 'create', component: CreateTaskComponent },
  { path: 'edit', component: EditTaskComponent},
  { path: 'admin', component: AdminComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
