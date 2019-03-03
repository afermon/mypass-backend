import { Moment } from 'moment';
import { ISecret } from 'app/shared/model//secret.model';
import { IUser } from 'app/core/user/user.model';

export interface IFolder {
    id?: number;
    name?: string;
    icon?: string;
    key?: string;
    created?: Moment;
    modified?: Moment;
    secrets?: ISecret[];
    ownerLogin?: string;
    ownerId?: number;
    sharedWiths?: IUser[];
}

export class Folder implements IFolder {
    constructor(
        public id?: number,
        public name?: string,
        public icon?: string,
        public key?: string,
        public created?: Moment,
        public modified?: Moment,
        public secrets?: ISecret[],
        public ownerLogin?: string,
        public ownerId?: number,
        public sharedWiths?: IUser[]
    ) {}
}
