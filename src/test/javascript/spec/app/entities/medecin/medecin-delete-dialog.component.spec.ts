/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MedStatTestModule } from '../../../test.module';
import { MedecinDeleteDialogComponent } from 'app/entities/medecin/medecin-delete-dialog.component';
import { MedecinService } from 'app/entities/medecin/medecin.service';

describe('Component Tests', () => {
    describe('Medecin Management Delete Component', () => {
        let comp: MedecinDeleteDialogComponent;
        let fixture: ComponentFixture<MedecinDeleteDialogComponent>;
        let service: MedecinService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MedStatTestModule],
                declarations: [MedecinDeleteDialogComponent]
            })
                .overrideTemplate(MedecinDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MedecinDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MedecinService);
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
