import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserService } from 'src/app/services/User/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})

/* this class is useful for managing the header */
export class HeaderComponent {
  @Input() sidebarActive = false;
  @Output() toggleSidebar = new EventEmitter();
  username!: string;

  /* this function is used to inject the services and get the user name */
  constructor(private userService: UserService) {
    this.userService.getUser().subscribe((user) => {
      if (user != null && user.name != undefined) {
        this.username = user.name;
      }
    })
  }
}
