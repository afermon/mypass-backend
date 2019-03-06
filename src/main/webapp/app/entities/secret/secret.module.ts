import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyPassSharedModule } from 'app/shared';
import { MyPassAdminModule } from 'app/admin/admin.module';
import {
    SecretComponent,
    SecretDetailComponent,
    SecretUpdateComponent,
    SecretDeletePopupComponent,
    SecretDeleteDialogComponent,
    secretRoute,
    secretPopupRoute
} from './';

const ENTITY_STATES = [...secretRoute, ...secretPopupRoute];

@NgModule({
    imports: [MyPassSharedModule, MyPassAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SecretComponent, SecretDetailComponent, SecretUpdateComponent, SecretDeleteDialogComponent, SecretDeletePopupComponent],
    entryComponents: [SecretComponent, SecretUpdateComponent, SecretDeleteDialogComponent, SecretDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyPassSecretModule {}
