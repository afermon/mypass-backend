import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ISecret } from 'app/shared/model/secret.model';
import { SecretService } from './secret.service';
import { IUser, UserService } from 'app/core';
import { IFolder } from 'app/shared/model/folder.model';
import { FolderService } from 'app/entities/folder';

@Component({
    selector: 'jhi-secret-update',
    templateUrl: './secret-update.component.html'
})
export class SecretUpdateComponent implements OnInit {
    secret: ISecret;
    isSaving: boolean;

    users: IUser[];

    folders: IFolder[];
    created: string;
    modified: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected secretService: SecretService,
        protected userService: UserService,
        protected folderService: FolderService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ secret }) => {
            this.secret = secret;
            this.created = this.secret.created != null ? this.secret.created.format(DATE_TIME_FORMAT) : null;
            this.modified = this.secret.modified != null ? this.secret.modified.format(DATE_TIME_FORMAT) : null;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.folderService.query().subscribe(
            (res: HttpResponse<IFolder[]>) => {
                this.folders = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.secret.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.secret.modified = this.modified != null ? moment(this.modified, DATE_TIME_FORMAT) : null;
        if (this.secret.id !== undefined) {
            this.subscribeToSaveResponse(this.secretService.update(this.secret));
        } else {
            this.subscribeToSaveResponse(this.secretService.create(this.secret));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISecret>>) {
        result.subscribe((res: HttpResponse<ISecret>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackFolderById(index: number, item: IFolder) {
        return item.id;
    }
}
