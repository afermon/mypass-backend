/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MyPassTestModule } from '../../../test.module';
import { SecretComponent } from 'app/entities/secret/secret.component';
import { SecretService } from 'app/entities/secret/secret.service';
import { Secret } from 'app/shared/model/secret.model';

describe('Component Tests', () => {
    describe('Secret Management Component', () => {
        let comp: SecretComponent;
        let fixture: ComponentFixture<SecretComponent>;
        let service: SecretService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MyPassTestModule],
                declarations: [SecretComponent],
                providers: []
            })
                .overrideTemplate(SecretComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SecretComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SecretService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Secret(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.secrets[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
