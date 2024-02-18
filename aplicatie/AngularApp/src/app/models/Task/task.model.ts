/* Task Model */
export class Task {
  id: number;
  title: string;
  description: string;
  startDate: Date;
  dueDate: Date;
  isCompleted: boolean;
  
  constructor(id: number, title: string, description: string, startDate: Date, dueDate: Date, isCompleted: boolean) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.startDate = startDate;
    this.dueDate = dueDate;
    this.isCompleted = isCompleted;
  }
}
