import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})

/* this class is useful for managing the tasks 
  and designing the tasks list*/
export class TasksComponent {
  @Input() selectedDayTasks: any[] = [];
  @Input() selectedDay!: number;
}
