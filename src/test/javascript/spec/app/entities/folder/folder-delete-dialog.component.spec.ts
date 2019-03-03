/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MyPassTestModule } from '../../../test.module';
import { FolderDeleteDialogComponent } from 'app/entities/folder/folder-delete-dialog.component';
import { FolderService } from 'app/entities/folder/folder.service';

describe('Component Tests', () => {
    describe('Folder Management Delete Component', () => {
        let comp: FolderDeleteDialogComponent;
        let fixture: ComponentFixture<FolderDeleteDialogComponent>;
        let service: FolderService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MyPassTestModule],
                declarations: [FolderDeleteDialogComponent]
            })
                .overrideTemplate(FolderDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FolderDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FolderService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
