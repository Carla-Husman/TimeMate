/* User model */
export class User {
  id!: number;
  username!: string;
  password!: string;
  email!: string;
  points!: number;

  constructor(id: number, username: string, password: string, email: string, points: number) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.points = points;
  }
}
