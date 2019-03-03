/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MyPassTestModule } from '../../../test.module';
import { FolderUpdateComponent } from 'app/entities/folder/folder-update.component';
import { FolderService } from 'app/entities/folder/folder.service';
import { Folder } from 'app/shared/model/folder.model';

describe('Component Tests', () => {
    describe('Folder Management Update Component', () => {
        let comp: FolderUpdateComponent;
        let fixture: ComponentFixture<FolderUpdateComponent>;
        let service: FolderService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MyPassTestModule],
                declarations: [FolderUpdateComponent]
            })
                .overrideTemplate(FolderUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FolderUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FolderService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Folder(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.folder = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Folder();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.folder = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
