import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, catchError, map, of } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authentication = "http://localhost:8080/auth/generateToken"

  constructor(private httpClient: HttpClient, private router: Router) { }

  /* this function checks if the token is expired */
  isExpired(): Observable<boolean> {
    const token = window.localStorage.getItem('token');
    return this.httpClient.get(`http://localhost:8080/auth/validateToken/${token}`, { observe: 'response' })
      .pipe(
        map((response: HttpResponse<any>) => {
          if (response.status === 200 && response.body != null) {
            return response.body;
          }
          return true;
        }),
        catchError((error) => {
          console.error('Validate failed:', error);
          return of(true);
        })
      )
  }

  // Function to check if the user is authenticated
  isAuthenticated(): boolean {
    const token = window.localStorage.getItem('token');
    return token !== null && token !== undefined;
  }

  /* this function logs in the user with the given username and password*/
  login(username: string, password: string): Observable<string> {
    const body = {
      username: username,
      password: password
    };
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.httpClient.post(this.authentication, body, { headers, observe: 'response', responseType: 'text' })
      .pipe(
        map((response: HttpResponse<string>) => {
          if (response.status === 200 && response.body != null) {
            window.localStorage.setItem("token", response.body);
            return "Success";
          }
          return "Username or password incorrect";
        }),
        catchError((error) => {
          if (error.status === 403)
            return of("YOU ARE BLOCKED FROM TIMEMATE! PLEASE CONTACT THE ADMINISTRATOR!");
          console.error('Authentication failed:', error);
          return of("Username or password incorrect");
        })
      );
  }

  /* this function logs out the user */
  logout() {
    window.localStorage.removeItem("token");
    window.localStorage.removeItem("id");
    this.router.navigate(['/account']);
  }
}
