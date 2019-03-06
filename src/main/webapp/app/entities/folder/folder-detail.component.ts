import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFolder } from 'app/shared/model/folder.model';

@Component({
    selector: 'jhi-folder-detail',
    templateUrl: './folder-detail.component.html'
})
export class FolderDetailComponent implements OnInit {
    folder: IFolder;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ folder }) => {
            this.folder = folder;
        });
    }

    previousState() {
        window.history.back();
    }
}
