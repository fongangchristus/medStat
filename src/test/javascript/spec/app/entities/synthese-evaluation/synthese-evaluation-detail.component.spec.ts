/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MedStatTestModule } from '../../../test.module';
import { SyntheseEvaluationDetailComponent } from 'app/entities/synthese-evaluation/synthese-evaluation-detail.component';
import { SyntheseEvaluation } from 'app/shared/model/synthese-evaluation.model';

describe('Component Tests', () => {
    describe('SyntheseEvaluation Management Detail Component', () => {
        let comp: SyntheseEvaluationDetailComponent;
        let fixture: ComponentFixture<SyntheseEvaluationDetailComponent>;
        const route = ({ data: of({ syntheseEvaluation: new SyntheseEvaluation(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MedStatTestModule],
                declarations: [SyntheseEvaluationDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SyntheseEvaluationDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SyntheseEvaluationDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.syntheseEvaluation).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
