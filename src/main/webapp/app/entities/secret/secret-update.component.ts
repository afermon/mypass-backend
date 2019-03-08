import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ISecret } from 'app/shared/model/secret.model';
import { SecretService } from './secret.service';
import { IFolder } from 'app/shared/model/folder.model';
import { FolderService } from 'app/entities/folder';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-secret-update',
    templateUrl: './secret-update.component.html'
})
export class SecretUpdateComponent implements OnInit {
    secret: ISecret;
    isSaving: boolean;

    folders: IFolder[];

    users: IUser[];
    modified: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected secretService: SecretService,
        protected folderService: FolderService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ secret }) => {
            this.secret = secret;
            this.modified = this.secret.modified != null ? this.secret.modified.format(DATE_TIME_FORMAT) : null;
        });
        this.folderService.query().subscribe(
            (res: HttpResponse<IFolder[]>) => {
                this.folders = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
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

    trackFolderById(index: number, item: IFolder) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
