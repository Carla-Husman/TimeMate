import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/User/user.model';
import { AdminService } from 'src/app/services/Admin/admin.service';
import { AuthService } from 'src/app/services/Auth/auth.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  // List of users
  users!: User[];

  constructor(private adminService: AdminService, private authService: AuthService) { }

  ngOnInit(): void {
    // Check if the token is expired and log out if it is
    this.authService.isExpired().subscribe((isExpired) => {
      if (isExpired) {
        this.authService.logout();
        return;
      }
    });

    // Load administrator profile on component initialization
    this.adminService.adminProfile().subscribe((users: User[]) => {
      // Update the list of users with the ones obtained from the service
      this.users = users;
    });
  }

  // Function to delete a user
  deleteUser(id: number) {
    // Call the service to block the user
    this.adminService.blockUser(id).subscribe();
  }

  // Function to log out the administrator
  logout() {
    // Remove token and admin indicator from local storage
    localStorage.removeItem('token');
    localStorage.removeItem('admin');
    
    // Redirect to the account login page
    window.location.href = '/account';
  }
}
