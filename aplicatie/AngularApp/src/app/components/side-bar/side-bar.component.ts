import { Component } from '@angular/core';
import { AuthService } from 'src/app/services/Auth/auth.service';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})

/* this class is useful for side-bar menu*/
export class SideBarComponent {

  /* this function is used to injects the services*/
  constructor(private authService: AuthService){}

  /*this function is used to call the logout function form the service*/
  onLogoutClick(){
    this.authService.logout();
  }
}
