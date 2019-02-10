import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IEvaluation } from 'app/shared/model/evaluation.model';
import { EvaluationService } from './evaluation.service';
import { ISyntheseEvaluation } from 'app/shared/model/synthese-evaluation.model';
import { SyntheseEvaluationService } from 'app/entities/synthese-evaluation';
import { IMedecin } from 'app/shared/model/medecin.model';
import { MedecinService } from 'app/entities/medecin';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { QuestionnaireService } from 'app/entities/questionnaire';

@Component({
    selector: 'jhi-evaluation-update',
    templateUrl: './evaluation-update.component.html'
})
export class EvaluationUpdateComponent implements OnInit {
    evaluation: IEvaluation;
    isSaving: boolean;

    syntheseevaluations: ISyntheseEvaluation[];

    medecins: IMedecin[];

    questionnaires: IQuestionnaire[];
    dateEvaluation: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected evaluationService: EvaluationService,
        protected syntheseEvaluationService: SyntheseEvaluationService,
        protected medecinService: MedecinService,
        protected questionnaireService: QuestionnaireService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ evaluation }) => {
            this.evaluation = evaluation;
            this.dateEvaluation = this.evaluation.dateEvaluation != null ? this.evaluation.dateEvaluation.format(DATE_TIME_FORMAT) : null;
        });
        this.syntheseEvaluationService
            .query({ filter: 'evaluation-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<ISyntheseEvaluation[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISyntheseEvaluation[]>) => response.body)
            )
            .subscribe(
                (res: ISyntheseEvaluation[]) => {
                    if (!this.evaluation.syntheseEvaluationId) {
                        this.syntheseevaluations = res;
                    } else {
                        this.syntheseEvaluationService
                            .find(this.evaluation.syntheseEvaluationId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<ISyntheseEvaluation>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<ISyntheseEvaluation>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: ISyntheseEvaluation) => (this.syntheseevaluations = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.medecinService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IMedecin[]>) => mayBeOk.ok),
                map((response: HttpResponse<IMedecin[]>) => response.body)
            )
            .subscribe((res: IMedecin[]) => (this.medecins = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.questionnaireService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IQuestionnaire[]>) => mayBeOk.ok),
                map((response: HttpResponse<IQuestionnaire[]>) => response.body)
            )
            .subscribe((res: IQuestionnaire[]) => (this.questionnaires = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.evaluation.dateEvaluation = this.dateEvaluation != null ? moment(this.dateEvaluation, DATE_TIME_FORMAT) : null;
        if (this.evaluation.id !== undefined) {
            this.subscribeToSaveResponse(this.evaluationService.update(this.evaluation));
        } else {
            this.subscribeToSaveResponse(this.evaluationService.create(this.evaluation));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvaluation>>) {
        result.subscribe((res: HttpResponse<IEvaluation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSyntheseEvaluationById(index: number, item: ISyntheseEvaluation) {
        return item.id;
    }

    trackMedecinById(index: number, item: IMedecin) {
        return item.id;
    }

    trackQuestionnaireById(index: number, item: IQuestionnaire) {
        return item.id;
    }
}
