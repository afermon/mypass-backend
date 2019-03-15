import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISecret } from 'app/shared/model/secret.model';
import { AccountService } from 'app/core';
import { SecretService } from './secret.service';

@Component({
    selector: 'jhi-secret',
    templateUrl: './secret.component.html'
})
export class SecretComponent implements OnInit, OnDestroy {
    secrets: ISecret[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected secretService: SecretService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.secretService.query().subscribe(
            (res: HttpResponse<ISecret[]>) => {
                this.secrets = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSecrets();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISecret) {
        return item.id;
    }

    registerChangeInSecrets() {
        this.eventSubscriber = this.eventManager.subscribe('secretListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
