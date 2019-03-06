/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MyPassTestModule } from '../../../test.module';
import { SecretDetailComponent } from 'app/entities/secret/secret-detail.component';
import { Secret } from 'app/shared/model/secret.model';

describe('Component Tests', () => {
    describe('Secret Management Detail Component', () => {
        let comp: SecretDetailComponent;
        let fixture: ComponentFixture<SecretDetailComponent>;
        const route = ({ data: of({ secret: new Secret(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MyPassTestModule],
                declarations: [SecretDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SecretDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SecretDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.secret).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
