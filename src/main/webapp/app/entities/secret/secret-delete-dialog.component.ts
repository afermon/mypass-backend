import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISecret } from 'app/shared/model/secret.model';
import { SecretService } from './secret.service';

@Component({
    selector: 'jhi-secret-delete-dialog',
    templateUrl: './secret-delete-dialog.component.html'
})
export class SecretDeleteDialogComponent {
    secret: ISecret;

    constructor(protected secretService: SecretService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.secretService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'secretListModification',
                content: 'Deleted an secret'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-secret-delete-popup',
    template: ''
})
export class SecretDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ secret }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SecretDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.secret = secret;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
