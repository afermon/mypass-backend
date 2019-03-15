import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MyPassFolderModule } from './folder/folder.module';
import { MyPassSecretModule } from './secret/secret.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        MyPassFolderModule,
        MyPassSecretModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyPassEntityModule {}
