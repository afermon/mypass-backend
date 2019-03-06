import { Moment } from 'moment';

export interface ISecret {
    id?: number;
    url?: string;
    name?: string;
    username?: string;
    password?: string;
    notes?: string;
    created?: Moment;
    modified?: Moment;
    ownerLogin?: string;
    ownerId?: number;
    folderName?: string;
    folderId?: number;
}

export class Secret implements ISecret {
    constructor(
        public id?: number,
        public url?: string,
        public name?: string,
        public username?: string,
        public password?: string,
        public notes?: string,
        public created?: Moment,
        public modified?: Moment,
        public ownerLogin?: string,
        public ownerId?: number,
        public folderName?: string,
        public folderId?: number
    ) {}
}
