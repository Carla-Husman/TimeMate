import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { User } from 'src/app/models/User/user.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private adminProfileUrl = 'http://localhost:8080/auth';

  constructor(private httpClient: HttpClient) { }

  adminProfile() : Observable<User[]> {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem('token') };
    return this.httpClient.get<User[]>(this.adminProfileUrl + "/admin/adminProfile", { headers, observe: 'response' })
      .pipe(
        map((response: HttpResponse<any[]>) => {
          if (response.status === 200 && response.body != null) {
            for (let i = 0; i < response.body.length; ++i) {
              response.body[i].username = response.body[i].name;
            }
            return response.body;
          }
          return [];
        }),
        catchError((error) => {
          console.error('Admin profile failed:', error);
          return of([]);
        })
      )
  }

  blockUser(id: number) {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem('token') };
    return this.httpClient.put(this.adminProfileUrl + '/admin/blockUser/' + id, null, { headers, observe: 'response', responseType: 'text' })
      .pipe(
        map((response: HttpResponse<any>) => {
          if (response.status === 200 && response.body != null) {
            return true;
          }
          return false;
        }),
        catchError((error) => {
          console.error('Block user failed:', error);
          return of(false);
        })
      )
  }
}
