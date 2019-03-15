import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IFolder } from 'app/shared/model/folder.model';
import { AccountService } from 'app/core';
import { FolderService } from './folder.service';

@Component({
    selector: 'jhi-folder',
    templateUrl: './folder.component.html'
})
export class FolderComponent implements OnInit, OnDestroy {
    folders: IFolder[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected folderService: FolderService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.folderService.query().subscribe(
            (res: HttpResponse<IFolder[]>) => {
                this.folders = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInFolders();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFolder) {
        return item.id;
    }

    registerChangeInFolders() {
        this.eventSubscriber = this.eventManager.subscribe('folderListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
