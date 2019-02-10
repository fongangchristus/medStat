/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MedStatTestModule } from '../../../test.module';
import { ReponseUpdateComponent } from 'app/entities/reponse/reponse-update.component';
import { ReponseService } from 'app/entities/reponse/reponse.service';
import { Reponse } from 'app/shared/model/reponse.model';

describe('Component Tests', () => {
    describe('Reponse Management Update Component', () => {
        let comp: ReponseUpdateComponent;
        let fixture: ComponentFixture<ReponseUpdateComponent>;
        let service: ReponseService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MedStatTestModule],
                declarations: [ReponseUpdateComponent]
            })
                .overrideTemplate(ReponseUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReponseUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReponseService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Reponse(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reponse = entity;
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
                    const entity = new Reponse();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reponse = entity;
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
