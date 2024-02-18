import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Task } from 'src/app/models/Task/task.model';
import { TaskService } from 'src/app/services/Task/task.service';

@Component({
  selector: 'app-edit-task',
  templateUrl: './edit-task.component.html',
  styleUrls: ['./edit-task.component.css']
})
export class EditTaskComponent {
  // Variables for managing the edit task
  taskName: string = '';
  description: string = '';
  startDate: string = '';
  endDate: string = '';
  startTime: string = '';
  endTime: string = '';
  isCompleted: boolean = false;
  task!: Task;

  /* Injecting services and retrieving task data from queryParams */
  constructor(private taskService: TaskService, private router: ActivatedRoute, private route: Router) {
    // Retrieving task data from query parameters
    this.router.queryParams.subscribe(params => {
      this.task = JSON.parse(params['task']);
    });

    // Initializing component variables with task data
    this.taskName = this.task.title;
    this.description = this.task.description;
    this.startDate = this.task.startDate.toString().split('T')[0];
    this.endDate = this.task.dueDate.toString().split('T')[0];
    this.startTime = this.task.startDate.toString().split('T')[1].split('.')[0];
    this.endTime = this.task.dueDate.toString().split('T')[1].split('.')[0];
    this.isCompleted = this.task.isCompleted;
  }

  /* Function to edit the task */
  editTask(): void {
    this.taskService.updateTask(
      this.task.id.toString(),
      this.taskName,
      this.description,
      this.task.startDate.toString().split('T')[0],
      this.endDate,
      this.task.startDate.toString().split('T')[1].split('.')[0],
      this.endTime,
      this.isCompleted
    ).subscribe((response) => {
      if (response === '') {
        // Navigate to the calendar page after successful task edit
        this.route.navigate(['/calendar']);
      } else {
        // Display an alert if there's an error during task edit
        alert(response);
      }
    });
  }
}
