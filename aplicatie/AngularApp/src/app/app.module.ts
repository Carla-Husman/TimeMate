import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { HeaderComponent } from './components/header/header.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { TasksComponent } from './components/tasks/tasks.component';
import { TaskComponent } from './components/tasks/task/task.component';
import { BadgesComponent } from './components/badges/badges.component';
import { BadgeComponent } from './components/badges/badge/badge.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EditModalComponent } from './components/profile/edit-modal/edit-modal.component';
import { RegisterComponent } from './components/register/register.component';
import { CreateTaskComponent } from './components/tasks/create-task/create-task.component';
import { EditTaskComponent } from './components/tasks/edit-task/edit-task.component';
import { AdminComponent } from './components/admin/admin.component';

@NgModule({
  declarations: [
    AppComponent,
    SideBarComponent,
    HeaderComponent,
    CalendarComponent,
    TasksComponent,
    TaskComponent,
    BadgesComponent,
    BadgeComponent,
    ProfileComponent,
    EditModalComponent,
    RegisterComponent,
    CreateTaskComponent,
    EditTaskComponent,
    AdminComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
