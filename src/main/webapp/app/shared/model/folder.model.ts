import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ISecret } from 'app/shared/model//secret.model';

export interface IFolder {
    id?: number;
    name?: string;
    icon?: string;
    key?: string;
    modified?: Moment;
    ownerLogin?: string;
    ownerId?: number;
    sharedWiths?: IUser[];
    secrets?: ISecret[];
}

export class Folder implements IFolder {
    constructor(
        public id?: number,
        public name?: string,
        public icon?: string,
        public key?: string,
        public modified?: Moment,
        public ownerLogin?: string,
        public ownerId?: number,
        public sharedWiths?: IUser[],
        public secrets?: ISecret[]
    ) {}
}
