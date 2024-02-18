/* Badge model */
export class Badge {
    index!: number;
    src!: string;
    title!: string;
    description!: string;
    necessaryPoints!: number;

    constructor(index: number, src: string, title: string, description: string, necessaryPoints: number) {
        this.index = index;
        this.src = src;
        this.title = title;
        this.description = description;
        this.necessaryPoints = necessaryPoints;
    }
}