import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService } from 'src/app/services/Task/task.service';

@Component({
  selector: 'app-create-task',
  templateUrl: './create-task.component.html',
  styleUrls: ['./create-task.component.css']
})
export class CreateTaskComponent implements OnInit {
  taskName: string = '';
  description: string = '';
  startDate: string = '';
  endDate: string = '';
  startTime: string = '';
  endTime: string = '';
  error: string = '';

  constructor(private taskService: TaskService, private router: ActivatedRoute, private route: Router) { }

  ngOnInit() {
    // Retrieve date information from query parameters and format the start date
    this.router.queryParams.subscribe(params => {
      let day = params['day'];
      let month = params['month'];

      // Add leading zero for single-digit day and month
      if (params['day'] < 10) {
        day = '0' + params['day'];
      }
      if (params['month'] < 10) {
        month = '0' + params['month'];
      }

      // Set the formatted start date
      this.startDate = params['year'] + '-' + month + '-' + day;
    });
  }

  /* Function to create a new task */
  createTask(): void {
    this.taskService.addTask(
      this.taskName,
      this.description,
      this.startDate,
      this.endDate,
      this.startTime,
      this.endTime
    ).subscribe((response) => {
      if (response === '') {
        // Navigate to the calendar page after successful task creation
        this.route.navigate(['/calendar']);
      } else {
        // Display an alert if there's an error during task creation
        alert(response);
      }
    });
  }
}
