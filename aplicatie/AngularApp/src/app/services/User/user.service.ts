import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../../models/User/user.model';
import { Router } from '@angular/router';
import { Observable, catchError, map, of } from 'rxjs';
import { UserInfo } from 'src/app/models/UserInfo/user-info.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  // JAVA SPRING BOOT CONTROLLER ENDPOINTS
  private userProfile = "http://localhost:8080/auth/user/userProfile";
  private addUser = "http://localhost:8080/auth/addNewUser";
  private updateUserURL = "http://localhost:8080/auth/user/updateProfile";

  /* Injecting the HttpClient service and the Router service */
  constructor(private httpClient: HttpClient, private router: Router) {}

  /* Function to create a new user */
  createUser(user: User): Observable<string> {
    const body = {
      name: user.username,
      password: user.password,
      email: user.email,
      roles: "ROLE_USER"
    };
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.httpClient.post(this.addUser, body, { headers, observe: 'response', responseType: 'text' })
      .pipe(
        map((response: HttpResponse<string>) => {
          if (response.status === 201 && response.body != null) {
            return "";
          }
          return "Err";
        }),
        catchError((error) => {
          console.error('Registration failed:', error);
          return of(error.error);
        })
      );
  }

  /* Function to get user information based on the authentication token */
  getUser(): Observable<UserInfo | null> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + window.localStorage.getItem('token')
    });

    return this.httpClient.get<any>(this.userProfile, { headers, observe: 'response' })
      .pipe(
        map((response: HttpResponse<any>) => {
          if (response.status === 200 && response.body != null) {
            return new UserInfo(
              response.body.user.id,
              response.body.user.name,
              response.body.user.email,
              response.body.user.streak,
              response.body.user.joinDate,
              response.body.progress,
              response.body.user.badges
            );
          }
          return null as any;
        }),
        catchError((error) => {
          return of(null);
        })
      );
  }

  /* Function to update user information */
  updateUser(newUsername: string, newEmail: string, currentPassword: string): Observable<string> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${window.localStorage.getItem('token')}`
    });

    const body = {
      newUsername: newUsername,
      newEmail: newEmail,
      currentPassword: currentPassword
    };

    return this.httpClient.put(this.updateUserURL, body, { headers, observe: 'response', responseType: 'text' })
      .pipe(
        map((response: HttpResponse<string>) => {
          if (response.status === 200 && response.body != null) {
            return "";
          }
          return "err";
        }),
        catchError((error) => {
          //console.error('Update task failed', error);
          return of("Wrong password!");
        })
      );
  }
}