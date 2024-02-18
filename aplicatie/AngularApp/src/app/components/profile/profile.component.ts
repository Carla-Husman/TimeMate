import { Component, OnInit } from '@angular/core';
import { UserInfo } from 'src/app/models/UserInfo/user-info.model';
import { AuthService } from 'src/app/services/Auth/auth.service';
import { ModalService } from 'src/app/services/Modal/modal.service';
import { UserService } from 'src/app/services/User/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

/* this class is useful for managing the profile page */
export class ProfileComponent implements OnInit {
  user!: UserInfo;
  lastBadge!: string;
  progressValue!: number;
  src!: string;

  /* this function is used to inject the services */
  constructor(private modalService: ModalService, private userService: UserService, private authService: AuthService) { }

  /* this function is used to get the user info */
  ngOnInit(): void {
    // Check if the token is expired and log out if it is
    this.authService.isExpired().subscribe((isExpired) => {
      if (isExpired) {
        this.authService.logout();
        return;
      }
    });

    this.userService.getUser().subscribe((user) => {
      if (user != null && user.progress != undefined) {
        this.user = user;
        localStorage.setItem('id', user.id.toString());
        this.progressValue = user.progress;
        user.badges.includes('+') ? this.lastBadge = user.badges.split('+')[user.badges.split('+').length - 1] :
          this.lastBadge = user.badges;
        this.src = `assets/badges/${this.lastBadge.replaceAll(" ", "")}.png`;
      }

      let stringWithSpaces = "";
      for (let i = 0; i < this.lastBadge.length; i++) {
        const currentChar = this.lastBadge[i];

        if (currentChar === currentChar.toUpperCase()) {
          stringWithSpaces += " ";
        }

        stringWithSpaces += currentChar;
      }

      this.lastBadge = stringWithSpaces;
    })
  }

  /* this function is used to open the modal */
  open() {
    this.modalService.open();
  }
}
