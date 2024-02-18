import { Component, OnInit } from '@angular/core';
import { TaskService } from 'src/app/services/Task/task.service';
import { Task } from 'src/app/models/Task/task.model';
import { Calendar } from 'src/app/models/Calendar/calendar.model';
import { Week } from 'src/app/models/Calendar/week.model';
import { Day } from 'src/app/models/Calendar/day.model';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/Auth/auth.service';
import { Observable, forkJoin } from 'rxjs';
import { UserService } from 'src/app/services/User/user.service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})

/* This class represents the calendar component */
export class CalendarComponent implements OnInit {

  /* Useful variables */
  daysOfWeek: string[] = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  currentDate: Date = new Date();
  selectedDayTasks!: Task[];
  monthAndYear!: string;
  selectedDay!: number;
  calendar!: Calendar;
  progressValue: number = 60;
  t!: Task[];

  /* Constructor function used to inject services */
  constructor(
    private taskService: TaskService,
    private router: Router,
    private authService: AuthService,
    private userService: UserService
  ) {
    this.currentDate = new Date();
    this.selectedDay = this.currentDate.getDate();
    this.initializeTasks(this.currentDate);
  }

  /* Asynchronously initialize tasks for the current month */
  async initializeTasks(currentDate: Date) {
    this.t = [];
    const tasksObservables: Observable<Task[]>[] = [];
    const lastDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0).getDate();
    for (let i = 1; i <= lastDayOfMonth; ++i) {
      const day = i < 10 ? '0' + i.toString() : i.toString();
      const month = currentDate.getMonth() + 1 < 10 ? '0' + (currentDate.getMonth() + 1).toString() : (currentDate.getMonth() + 1).toString();
      const date = `${currentDate.getFullYear()}-${month}-${day}`;
      const observable = this.taskService.tasksForThisMonth(date);
      tasksObservables.push(observable);
    }

    try {
      const tasksArrays = await forkJoin(tasksObservables).toPromise();
      // Concatenate all task arrays into a single array
      if (tasksArrays != undefined)
        this.t = tasksArrays.reduce((accumulator, currentArray) => accumulator.concat(currentArray), []);
    } catch (error) {
      // Handle errors if needed
      // console.error('Error fetching tasks', error);
    }

    this.userService.getUser().subscribe((user) => {
      if (user != undefined && user.progress != undefined) {
        this.progressValue = user.progress;
      }
    });

    this.generateCalendar();
  }

  /* ngOnInit is called when the component is initialized */
  ngOnInit() {
    // Check if the token is expired and log out if it is
    this.authService.isExpired().subscribe((isExpired) => {
      if (isExpired) {
        this.authService.logout();
        return;
      }
    });
    
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/account']);
    }
  }

  /* Display tasks for the selected day when a day is clicked */
  showTasksForDay(day: Day) {
    if (day.otherMonth) {
      // Set the currentDate to the selected day
      this.currentDate = new Date(day.year, day.month, day.day);

      // Update the calendar
      this.initializeTasks(this.currentDate);
    }

    // Update the selectedDayTasks
    this.selectedDay = day.day;

    this.selectedDayTasks = this.t.filter(task => {
      const startDate = new Date(task.startDate);
      return (
        startDate.getFullYear() === this.currentDate.getFullYear() &&
        startDate.getMonth() === this.currentDate.getMonth() &&
        startDate.getDate() === day.day
      );
    });
  }

  /* Move to the previous month when the previous month button is clicked */
  goToPreviousMonth() {
    this.currentDate.setMonth(this.currentDate.getMonth() - 1);
    this.initializeTasks(this.currentDate);
  }

  /* Move to the next month when the next month button is clicked */
  goToNextMonth() {
    this.currentDate.setMonth(this.currentDate.getMonth() + 1);
    this.initializeTasks(this.currentDate);
  }

  /* Render the calendar */
  generateCalendar() {
    this.calendar = new Calendar(); // Reset calendar
    const currentMonth = this.currentDate.getMonth();
    const currentYear = this.currentDate.getFullYear();

    this.monthAndYear = this.getMonthAndYear(currentMonth, currentYear);

    const firstDayOfMonth = new Date(currentYear, currentMonth, 1);
    const lastDayOfMonth = new Date(currentYear, currentMonth + 1, 0);
    const numberOfDaysInMonth = lastDayOfMonth.getDate();
    const firstDayOfWeek = firstDayOfMonth.getDay();

    let dayCounter = 1;

    for (let i = 0; i < 6; i++) { // 6 weeks in the calendar
      let currentWeek: Week = new Week();

      for (let j = 0; j < 7; j++) { // 7 days in a week
        let day: Day;

        if (dayCounter > firstDayOfWeek) { // Add days of the current month
          day = new Day(
            dayCounter - firstDayOfWeek,
            currentMonth,
            currentYear,
            (i === 0 && j < firstDayOfWeek) || (dayCounter > numberOfDaysInMonth + firstDayOfWeek)
          );
        } else { // Add days of the previous month
          var x = new Date();
          x.setDate(0);
          day = new Day(
            x.getDate() - firstDayOfWeek + dayCounter,
            (currentMonth === 0) ? 11 : currentMonth - 1,
            (currentMonth === 0) ? currentYear - 1 : currentYear,
            (i === 0 && j < firstDayOfWeek) || (dayCounter > numberOfDaysInMonth + firstDayOfWeek),
          );
        }

        currentWeek.days.push(day);
        dayCounter++;

        if (dayCounter > numberOfDaysInMonth + firstDayOfWeek) {
          break;
        }
      }

      this.calendar.weeks.push(currentWeek);
      if (dayCounter > numberOfDaysInMonth + firstDayOfWeek) {
        break;
      }
    }

    // Add days of the next month
    if (this.calendar.weeks[this.calendar.weeks.length - 1].days.length < 7) {
      let day = 1;
      for (let i = this.calendar.weeks[this.calendar.weeks.length - 1].days.length; i < 7; i++) {
        this.calendar.weeks[this.calendar.weeks.length - 1].days.push(new Day(
          day++,
          (currentMonth === 11) ? 0 : currentMonth + 1,
          (currentMonth === 11) ? currentYear + 1 : currentYear,
          true
        ));
      }
    }

    this.showTasksForDay(new Day(
      this.currentDate.getDate(),
      currentMonth,
      currentYear,
      false
    ));
  }

  /* Return the month and year as a string */
  getMonthAndYear(month: number, year: number): string {
    const months = [
      'January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'
    ];

    return `${months[month]} ${year}`;
  }

  /* Return true if the day has a task */
  hasTask(day: Day): boolean {
    if (!day.otherMonth) {
      this.selectedDay = day.day;

      const tasksForDay = this.t.filter(task => {
        const startDate = new Date(task.startDate);
        return (
          startDate.getFullYear() === this.currentDate.getFullYear() &&
          startDate.getMonth() === this.currentDate.getMonth() &&
          startDate.getDate() === day.day
        );
      });
      return tasksForDay.length > 0;
    }

    return false;
  }

  /* Redirect to the task creation page on double-click */
  redirectToPageOnDoubleClick(day: Day) {
    if (!day.otherMonth) {
      this.router.navigate(['/create'], {
        queryParams: {
          day: day.day,
          month: day.month + 1,
          year: day.year
        }
      });
    }
  }
}
