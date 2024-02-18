/* UserInfo model */
export class UserInfo {
    id!: number;
    name!: string;
    email!: string;
    streak!: number;
    joinDate!: Date;
    progress!: number;
    badges!: string;

    constructor(id: number, name: string, email: string, streak: number, joinDate: Date, progress: number, badges: string) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.streak = streak;
        this.joinDate = joinDate;
        this.progress = progress;
        this.badges = badges;
    }
}
