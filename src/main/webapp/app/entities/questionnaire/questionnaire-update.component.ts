import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { QuestionnaireService } from './questionnaire.service';

@Component({
    selector: 'jhi-questionnaire-update',
    templateUrl: './questionnaire-update.component.html'
})
export class QuestionnaireUpdateComponent implements OnInit {
    questionnaire: IQuestionnaire;
    isSaving: boolean;

    constructor(protected questionnaireService: QuestionnaireService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ questionnaire }) => {
            this.questionnaire = questionnaire;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.questionnaire.id !== undefined) {
            this.subscribeToSaveResponse(this.questionnaireService.update(this.questionnaire));
        } else {
            this.subscribeToSaveResponse(this.questionnaireService.create(this.questionnaire));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionnaire>>) {
        result.subscribe((res: HttpResponse<IQuestionnaire>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
