/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MedStatTestModule } from '../../../test.module';
import { MedecinDetailComponent } from 'app/entities/medecin/medecin-detail.component';
import { Medecin } from 'app/shared/model/medecin.model';

describe('Component Tests', () => {
    describe('Medecin Management Detail Component', () => {
        let comp: MedecinDetailComponent;
        let fixture: ComponentFixture<MedecinDetailComponent>;
        const route = ({ data: of({ medecin: new Medecin(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MedStatTestModule],
                declarations: [MedecinDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MedecinDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MedecinDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.medecin).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
