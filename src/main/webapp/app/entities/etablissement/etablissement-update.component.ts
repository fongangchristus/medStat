import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IEtablissement } from 'app/shared/model/etablissement.model';
import { EtablissementService } from './etablissement.service';
import { IMedecin } from 'app/shared/model/medecin.model';
import { MedecinService } from 'app/entities/medecin';
import { ILocalisation } from 'app/shared/model/localisation.model';
import { LocalisationService } from 'app/entities/localisation';

@Component({
    selector: 'jhi-etablissement-update',
    templateUrl: './etablissement-update.component.html'
})
export class EtablissementUpdateComponent implements OnInit {
    etablissement: IEtablissement;
    isSaving: boolean;

    medecins: IMedecin[];

    localisations: ILocalisation[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected etablissementService: EtablissementService,
        protected medecinService: MedecinService,
        protected localisationService: LocalisationService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ etablissement }) => {
            this.etablissement = etablissement;
        });
        this.medecinService
            .query({ filter: 'etablissement-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IMedecin[]>) => mayBeOk.ok),
                map((response: HttpResponse<IMedecin[]>) => response.body)
            )
            .subscribe(
                (res: IMedecin[]) => {
                    if (!this.etablissement.medecinId) {
                        this.medecins = res;
                    } else {
                        this.medecinService
                            .find(this.etablissement.medecinId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IMedecin>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IMedecin>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IMedecin) => (this.medecins = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.localisationService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ILocalisation[]>) => mayBeOk.ok),
                map((response: HttpResponse<ILocalisation[]>) => response.body)
            )
            .subscribe((res: ILocalisation[]) => (this.localisations = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.etablissement.id !== undefined) {
            this.subscribeToSaveResponse(this.etablissementService.update(this.etablissement));
        } else {
            this.subscribeToSaveResponse(this.etablissementService.create(this.etablissement));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtablissement>>) {
        result.subscribe((res: HttpResponse<IEtablissement>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackMedecinById(index: number, item: IMedecin) {
        return item.id;
    }

    trackLocalisationById(index: number, item: ILocalisation) {
        return item.id;
    }
}
