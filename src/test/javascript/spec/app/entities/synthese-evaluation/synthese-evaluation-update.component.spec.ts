/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MedStatTestModule } from '../../../test.module';
import { SyntheseEvaluationUpdateComponent } from 'app/entities/synthese-evaluation/synthese-evaluation-update.component';
import { SyntheseEvaluationService } from 'app/entities/synthese-evaluation/synthese-evaluation.service';
import { SyntheseEvaluation } from 'app/shared/model/synthese-evaluation.model';

describe('Component Tests', () => {
    describe('SyntheseEvaluation Management Update Component', () => {
        let comp: SyntheseEvaluationUpdateComponent;
        let fixture: ComponentFixture<SyntheseEvaluationUpdateComponent>;
        let service: SyntheseEvaluationService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MedStatTestModule],
                declarations: [SyntheseEvaluationUpdateComponent]
            })
                .overrideTemplate(SyntheseEvaluationUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SyntheseEvaluationUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SyntheseEvaluationService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SyntheseEvaluation(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.syntheseEvaluation = entity;
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
                    const entity = new SyntheseEvaluation();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.syntheseEvaluation = entity;
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
