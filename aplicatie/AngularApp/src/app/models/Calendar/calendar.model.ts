import { Week } from './week.model';

/* calendar model*/
export class Calendar {
    weeks!: Week[];

    constructor() {
        this.weeks = [];
    }
}
