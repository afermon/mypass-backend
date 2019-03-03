/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MyPassTestModule } from '../../../test.module';
import { FolderComponent } from 'app/entities/folder/folder.component';
import { FolderService } from 'app/entities/folder/folder.service';
import { Folder } from 'app/shared/model/folder.model';

describe('Component Tests', () => {
    describe('Folder Management Component', () => {
        let comp: FolderComponent;
        let fixture: ComponentFixture<FolderComponent>;
        let service: FolderService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MyPassTestModule],
                declarations: [FolderComponent],
                providers: []
            })
                .overrideTemplate(FolderComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FolderComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FolderService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Folder(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.folders[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
