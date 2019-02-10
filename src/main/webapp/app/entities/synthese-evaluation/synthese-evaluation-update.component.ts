import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ISyntheseEvaluation } from 'app/shared/model/synthese-evaluation.model';
import { SyntheseEvaluationService } from './synthese-evaluation.service';

@Component({
    selector: 'jhi-synthese-evaluation-update',
    templateUrl: './synthese-evaluation-update.component.html'
})
export class SyntheseEvaluationUpdateComponent implements OnInit {
    syntheseEvaluation: ISyntheseEvaluation;
    isSaving: boolean;

    constructor(protected syntheseEvaluationService: SyntheseEvaluationService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ syntheseEvaluation }) => {
            this.syntheseEvaluation = syntheseEvaluation;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.syntheseEvaluation.id !== undefined) {
            this.subscribeToSaveResponse(this.syntheseEvaluationService.update(this.syntheseEvaluation));
        } else {
            this.subscribeToSaveResponse(this.syntheseEvaluationService.create(this.syntheseEvaluation));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISyntheseEvaluation>>) {
        result.subscribe((res: HttpResponse<ISyntheseEvaluation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
