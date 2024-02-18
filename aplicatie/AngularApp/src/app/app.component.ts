import { Component, HostListener, OnInit } from '@angular/core';
import { UserService } from './services/User/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  sidebarActive = false;
  isUserLoggedIn = false;
  title = "TimeMate";

  constructor(private userService: UserService) { }

  isLogedIn():boolean{
    if (window.localStorage.getItem('token') !== null && window.localStorage.getItem('token') !== undefined && window.localStorage.getItem('admin') == undefined) {
      return true;
    } else{
      return false;
    }
  }
}
