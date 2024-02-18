import { Component } from '@angular/core';
import { Badge } from 'src/app/models/Bagde/badge.model';
import { AuthService } from 'src/app/services/Auth/auth.service';
import { BadgeServiceService } from 'src/app/services/Badge/badge-service.service';
import { UserService } from 'src/app/services/User/user.service';

@Component({
  selector: 'app-badges',
  templateUrl: './badges.component.html',
  styleUrls: ['./badges.component.css']
})

/* this class is useful for managing the badges page */
export class BadgesComponent {
  badges: Badge[][] = [];
  points!: number;
  userBadges!: string[];
  
  /* this function is used to inject the services */
  constructor(private badgeService: BadgeServiceService, private userService: UserService, private authService: AuthService) {
    this.userService.getUser().subscribe((user) => {
      this.points = user?.streak!;
    })
  }

  /* this function is used to initialize the variables */
  ngOnInit() {
    // Check if the token is expired and log out if it is
    this.authService.isExpired().subscribe((isExpired) => {
      if (isExpired) {
        this.authService.logout();
        return;
      }
    });

    this.userService.getUser().subscribe((user) => {
      if (user != null) {
        this.userBadges = user.badges.split('+');
      }
    })

    this.badgeService.getBadges().subscribe((badges) => {
      this.badges = this.makeMatrix(badges, 4);
    })
  }

  /* this function is used to make a matrix of badges to be displayed in the badges page */
  makeMatrix<T>(array: T[], size: number): T[][] {
    return Array.from({ length: Math.ceil(array.length / size) }, (v, i) =>
      array.slice(i * size, i * size + size)
    );
  }

  /* this method is used to check if the badge is unlocked*/
  isUnlocked(badgeName: any): boolean {
    return this.badgeService.isUnlocked(this.userBadges, badgeName.name);
  }
}
