import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Folder } from 'app/shared/model/folder.model';
import { FolderService } from './folder.service';
import { FolderComponent } from './folder.component';
import { FolderDetailComponent } from './folder-detail.component';
import { FolderUpdateComponent } from './folder-update.component';
import { FolderDeletePopupComponent } from './folder-delete-dialog.component';
import { IFolder } from 'app/shared/model/folder.model';

@Injectable({ providedIn: 'root' })
export class FolderResolve implements Resolve<IFolder> {
    constructor(private service: FolderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Folder> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Folder>) => response.ok),
                map((folder: HttpResponse<Folder>) => folder.body)
            );
        }
        return of(new Folder());
    }
}

export const folderRoute: Routes = [
    {
        path: 'folder',
        component: FolderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mypassApp.folder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'folder/:id/view',
        component: FolderDetailComponent,
        resolve: {
            folder: FolderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mypassApp.folder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'folder/new',
        component: FolderUpdateComponent,
        resolve: {
            folder: FolderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mypassApp.folder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'folder/:id/edit',
        component: FolderUpdateComponent,
        resolve: {
            folder: FolderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mypassApp.folder.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const folderPopupRoute: Routes = [
    {
        path: 'folder/:id/delete',
        component: FolderDeletePopupComponent,
        resolve: {
            folder: FolderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mypassApp.folder.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
