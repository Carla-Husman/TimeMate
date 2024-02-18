import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { Badge } from 'src/app/models/Bagde/badge.model';

@Injectable({
  providedIn: 'root'
})
export class BadgeServiceService {
  // URL to the badges endpoint
  private badgeURL = "http://localhost:8080/badges";

  /* this function is used to initialize the variables */
  constructor(private httpClient: HttpClient) { }

  /* this method return all the badges form the database */
  getBadges(): Observable<Badge[]> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + window.localStorage.getItem('token')
    });

    return this.httpClient.get<Badge[]>(this.badgeURL, { headers, observe: 'response' })
      .pipe(
        map((response: HttpResponse<any[]>) => {
          if (response.status === 200 && response.body != null) {
            console.log(response.body);
            for (let i = 0; i < response.body.length; ++i) {
              console.log(response.body[i].name!);
              response.body[i].src = `assets/badges/${response.body[i].name!.replaceAll(" ", "")}.png`;
            }
            return response.body;
          }
          return [];
        }),
        catchError((error) => {
          console.error('Badges failed:', error);
          return of([]);
        })
      )
  }

  /* this verifies if the badge is unlocked */
  isUnlocked(userBadges: string[], badgeName: string): boolean {
    return userBadges.includes(badgeName.replaceAll(" ", "")) ? true : false;
  }
}
