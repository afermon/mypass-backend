import { Moment } from 'moment';

export interface INotification {
    id?: number;
    title?: string;
    content?: string;
    created?: Moment;
    read?: boolean;
    userLogin?: string;
    userId?: number;
}

export class Notification implements INotification {
    constructor(
        public id?: number,
        public title?: string,
        public content?: string,
        public created?: Moment,
        public read?: boolean,
        public userLogin?: string,
        public userId?: number
    ) {
        this.read = this.read || false;
    }
}
