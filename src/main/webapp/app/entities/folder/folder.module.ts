import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyPassSharedModule } from 'app/shared';
import { MyPassAdminModule } from 'app/admin/admin.module';
import {
    FolderComponent,
    FolderDetailComponent,
    FolderUpdateComponent,
    FolderDeletePopupComponent,
    FolderDeleteDialogComponent,
    folderRoute,
    folderPopupRoute
} from './';

const ENTITY_STATES = [...folderRoute, ...folderPopupRoute];

@NgModule({
    imports: [MyPassSharedModule, MyPassAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [FolderComponent, FolderDetailComponent, FolderUpdateComponent, FolderDeleteDialogComponent, FolderDeletePopupComponent],
    entryComponents: [FolderComponent, FolderUpdateComponent, FolderDeleteDialogComponent, FolderDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyPassFolderModule {}
