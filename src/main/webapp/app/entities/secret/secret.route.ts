import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Secret } from 'app/shared/model/secret.model';
import { SecretService } from './secret.service';
import { SecretComponent } from './secret.component';
import { SecretDetailComponent } from './secret-detail.component';
import { SecretUpdateComponent } from './secret-update.component';
import { SecretDeletePopupComponent } from './secret-delete-dialog.component';
import { ISecret } from 'app/shared/model/secret.model';

@Injectable({ providedIn: 'root' })
export class SecretResolve implements Resolve<ISecret> {
    constructor(private service: SecretService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Secret> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Secret>) => response.ok),
                map((secret: HttpResponse<Secret>) => secret.body)
            );
        }
        return of(new Secret());
    }
}

export const secretRoute: Routes = [
    {
        path: 'secret',
        component: SecretComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mypassApp.secret.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'secret/:id/view',
        component: SecretDetailComponent,
        resolve: {
            secret: SecretResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mypassApp.secret.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'secret/new',
        component: SecretUpdateComponent,
        resolve: {
            secret: SecretResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mypassApp.secret.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'secret/:id/edit',
        component: SecretUpdateComponent,
        resolve: {
            secret: SecretResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mypassApp.secret.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const secretPopupRoute: Routes = [
    {
        path: 'secret/:id/delete',
        component: SecretDeletePopupComponent,
        resolve: {
            secret: SecretResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mypassApp.secret.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
