/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MyPassTestModule } from '../../../test.module';
import { SecretUpdateComponent } from 'app/entities/secret/secret-update.component';
import { SecretService } from 'app/entities/secret/secret.service';
import { Secret } from 'app/shared/model/secret.model';

describe('Component Tests', () => {
    describe('Secret Management Update Component', () => {
        let comp: SecretUpdateComponent;
        let fixture: ComponentFixture<SecretUpdateComponent>;
        let service: SecretService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MyPassTestModule],
                declarations: [SecretUpdateComponent]
            })
                .overrideTemplate(SecretUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SecretUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SecretService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Secret(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.secret = entity;
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
                    const entity = new Secret();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.secret = entity;
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
