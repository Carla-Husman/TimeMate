import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { Task } from 'src/app/models/Task/task.model';
import { UserService } from '../User/user.service';

@Injectable({
  providedIn: 'root'
})

/* this service is used to manage the tasks */
export class TaskService {
  // URL to the tasks endpoint
  private tasksUrl = 'http://localhost:8080/dashboard';

  constructor(private http: HttpClient, private userService: UserService) { }

  /* this method is called when a day is clicked to add a new task*/
  addTask(title: string, description: string, startDate: string, endDate: string, startTime: string, endTime: string): Observable<string> {
    if (title === '' || description === '' || startDate === '' || endDate === '' || startTime === '' || endTime === '') {
      return of('All fields are required!');
    }

    const body = {
      title: title,
      description: description,
      startDate: startDate + "T" + startTime,
      dueDate: endDate + "T" + endTime,
    };

    const id = window.localStorage.getItem('id');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${window.localStorage.getItem('token')}`
    });

    return this.http.post(`${this.tasksUrl}/${id}/addTask`, body, { headers, observe: 'response', responseType: 'text' })
      .pipe(
        map((response: HttpResponse<string>) => {
          if (response.status === 200 && response.body != null) {
            return response.body;
          }
          return "";
        }),
        catchError((error) => {
          console.error('Create task failed', error);
          return of(error.error);
        })
      )
  }

  /* this method is called when a task is updated */
  updateTask(id: string, title: string, description: string, startD: string, endD: string, startT: string, endT: string, IsCompleted: boolean): Observable<string> {
    if (title === '' || description === '' || startD === '' || endD === '' || startT === '' || endT === '') {
      return of('All fields are required!');
    }

    const body = {
      id: id,
      userID: window.localStorage.getItem('id'),
      title: title,
      description: description,
      startDate: startD + 'T' + startT,
      dueDate: startD + 'T' + endT,
      isCompleted: IsCompleted
    };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${window.localStorage.getItem('token')}`
    });

    return this.http.put(`${this.tasksUrl}/tasks/${id}`, body, { headers, observe: 'response', responseType: 'text' })
      .pipe(
        map((response: HttpResponse<string>) => {
          if (response.status === 200 && response.body != null) {
            return "";
          }
          return "err";
        }),
        catchError((error) => {
          //console.error('Update task failed', error);
          return of(error.error);
        })
      )
  }

  /* this method obtains all the tasks from the database for a specific date */
  tasksForThisMonth(date: string): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.tasksUrl}/${window.localStorage.getItem('id')}/tasks?date=${date}`, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${window.localStorage.getItem('token')}`
      }), observe: 'response'
    })
      .pipe(
        map((response: HttpResponse<Task[]>) => {
          if (response.status === 200 && response.body != null) {
            return response.body;
          }
          return []
        }),
        catchError((error) => {
          //console.error('Get tasks failed', error);
          return of([]);
        })
      )
  }
}
