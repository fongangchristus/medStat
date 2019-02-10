import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ILocalisation } from 'app/shared/model/localisation.model';
import { LocalisationService } from './localisation.service';

@Component({
    selector: 'jhi-localisation-update',
    templateUrl: './localisation-update.component.html'
})
export class LocalisationUpdateComponent implements OnInit {
    localisation: ILocalisation;
    isSaving: boolean;

    constructor(protected localisationService: LocalisationService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ localisation }) => {
            this.localisation = localisation;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.localisation.id !== undefined) {
            this.subscribeToSaveResponse(this.localisationService.update(this.localisation));
        } else {
            this.subscribeToSaveResponse(this.localisationService.create(this.localisation));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocalisation>>) {
        result.subscribe((res: HttpResponse<ILocalisation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
