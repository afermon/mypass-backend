import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MyPassSecretModule } from './secret/secret.module';
import { MyPassFolderModule } from './folder/folder.module';
import { MyPassNotificationModule } from './notification/notification.module';
import { MyPassSecretModule } from './secret/secret.module';
import { MyPassFolderModule } from './folder/folder.module';
import { MyPassSecretModule } from './secret/secret.module';
import { MyPassFolderModule } from './folder/folder.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        MyPassSecretModule,
        MyPassFolderModule,
        MyPassNotificationModule,
        MyPassFolderModule,
        MyPassFolderModule,
        MyPassNotificationModule,
        MyPassSecretModule,
        MyPassSecretModule,
        MyPassFolderModule,
        MyPassSecretModule,
        MyPassFolderModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyPassEntityModule {}
