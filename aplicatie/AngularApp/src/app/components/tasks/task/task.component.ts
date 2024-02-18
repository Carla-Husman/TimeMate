import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Task } from 'src/app/models/Task/task.model';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})

/* this class is useful for designing a single task */
export class TaskComponent {
  @Input() task!: Task;

  colorPaletteForTask: string[] = [
    '#E9EAF4', // light purple
    '#FFEEEA', // light red
    '#CDF2E0', // light green
    '#FFF9EC', // light yellow
    '#EBFAFE', // light blue
  ];
  colorPaletteForEllipse: string[] = [
    '#BABDDB',
    '#FFCABD',
    '#A8E1C5',
    '#FFE8B2',
    '#C0EFFC',
  ];
  colorPaletteForText: string[] = [
    '#4D56A2',
    '#FD6540',
    '#04D76F',
    '#F0B604',
    '#07CEDA',
  ];

  index = 0;

  constructor(private router: Router) { }

  /* this function return a random color from the color palette
    for the task background */
  randomColorForBackground(): string {
    const randomIndex = Math.floor(Math.random() * this.colorPaletteForTask.length);
    this.index = randomIndex;
    return this.colorPaletteForTask[randomIndex];
  }

  /* this function return a random color from the color palette
    for the ellipse background */
  randomColorForEllipse(): string {
    return this.colorPaletteForEllipse[this.index];
  }

  /* this function return a random color from the color palette
    for the text */
  randomColorForText(): string {
    return this.colorPaletteForText[this.index];
  }

  /* this function is used to navigate to edit task page */
  editTask(task: Task) {
    this.router.navigate(['/edit'], {
      queryParams: {
        task: JSON.stringify(task)
      }
    });
  }
}
