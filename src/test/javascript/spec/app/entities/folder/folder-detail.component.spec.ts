/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MyPassTestModule } from '../../../test.module';
import { FolderDetailComponent } from 'app/entities/folder/folder-detail.component';
import { Folder } from 'app/shared/model/folder.model';

describe('Component Tests', () => {
    describe('Folder Management Detail Component', () => {
        let comp: FolderDetailComponent;
        let fixture: ComponentFixture<FolderDetailComponent>;
        const route = ({ data: of({ folder: new Folder(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MyPassTestModule],
                declarations: [FolderDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FolderDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FolderDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.folder).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
