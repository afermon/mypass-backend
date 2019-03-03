/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MyPassTestModule } from '../../../test.module';
import { SecretDeleteDialogComponent } from 'app/entities/secret/secret-delete-dialog.component';
import { SecretService } from 'app/entities/secret/secret.service';

describe('Component Tests', () => {
    describe('Secret Management Delete Component', () => {
        let comp: SecretDeleteDialogComponent;
        let fixture: ComponentFixture<SecretDeleteDialogComponent>;
        let service: SecretService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MyPassTestModule],
                declarations: [SecretDeleteDialogComponent]
            })
                .overrideTemplate(SecretDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SecretDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SecretService);
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
