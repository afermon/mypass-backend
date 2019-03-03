import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISecret } from 'app/shared/model/secret.model';

@Component({
    selector: 'jhi-secret-detail',
    templateUrl: './secret-detail.component.html'
})
export class SecretDetailComponent implements OnInit {
    secret: ISecret;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ secret }) => {
            this.secret = secret;
        });
    }

    previousState() {
        window.history.back();
    }
}
