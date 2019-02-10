/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MedStatTestModule } from '../../../test.module';
import { EtablissementDeleteDialogComponent } from 'app/entities/etablissement/etablissement-delete-dialog.component';
import { EtablissementService } from 'app/entities/etablissement/etablissement.service';

describe('Component Tests', () => {
    describe('Etablissement Management Delete Component', () => {
        let comp: EtablissementDeleteDialogComponent;
        let fixture: ComponentFixture<EtablissementDeleteDialogComponent>;
        let service: EtablissementService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MedStatTestModule],
                declarations: [EtablissementDeleteDialogComponent]
            })
                .overrideTemplate(EtablissementDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EtablissementDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EtablissementService);
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
