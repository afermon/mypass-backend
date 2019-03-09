import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IFolder } from 'app/shared/model/folder.model';
import { FolderService } from './folder.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-folder-update',
    templateUrl: './folder-update.component.html'
})
export class FolderUpdateComponent implements OnInit {
    folder: IFolder;
    isSaving: boolean;

    users: IUser[];
    modified: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected folderService: FolderService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ folder }) => {
            this.folder = folder;
            this.modified = this.folder.modified != null ? this.folder.modified.format(DATE_TIME_FORMAT) : null;
        });
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
        this.folder.modified = this.modified != null ? moment(this.modified, DATE_TIME_FORMAT) : null;
        if (this.folder.id !== undefined) {
            this.subscribeToSaveResponse(this.folderService.update(this.folder));
        } else {
            this.subscribeToSaveResponse(this.folderService.create(this.folder));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IFolder>>) {
        result.subscribe((res: HttpResponse<IFolder>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
