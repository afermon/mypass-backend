import { Moment } from 'moment';

export interface ISecret {
    id?: number;
    url?: string;
    name?: string;
    username?: string;
    password?: string;
    notes?: string;
    modified?: Moment;
    folderName?: string;
    folderId?: number;
    ownerLogin?: string;
    ownerId?: number;
}

export class Secret implements ISecret {
    constructor(
        public id?: number,
        public url?: string,
        public name?: string,
        public username?: string,
        public password?: string,
        public notes?: string,
        public modified?: Moment,
        public folderName?: string,
        public folderId?: number,
        public ownerLogin?: string,
        public ownerId?: number
    ) {}
}
