/* Day model */
export class Day {
    day!: number;
    month!: number;
    year!: number;
    otherMonth!: boolean;

    constructor(day: number, month: number, year: number, otherMonth: boolean) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.otherMonth = otherMonth;
    }
}
