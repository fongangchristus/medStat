/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MedStatTestModule } from '../../../test.module';
import { SyntheseEvaluationDeleteDialogComponent } from 'app/entities/synthese-evaluation/synthese-evaluation-delete-dialog.component';
import { SyntheseEvaluationService } from 'app/entities/synthese-evaluation/synthese-evaluation.service';

describe('Component Tests', () => {
    describe('SyntheseEvaluation Management Delete Component', () => {
        let comp: SyntheseEvaluationDeleteDialogComponent;
        let fixture: ComponentFixture<SyntheseEvaluationDeleteDialogComponent>;
        let service: SyntheseEvaluationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MedStatTestModule],
                declarations: [SyntheseEvaluationDeleteDialogComponent]
            })
                .overrideTemplate(SyntheseEvaluationDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SyntheseEvaluationDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SyntheseEvaluationService);
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
