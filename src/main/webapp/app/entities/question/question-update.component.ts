import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IQuestion } from 'app/shared/model/question.model';
import { QuestionService } from './question.service';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { QuestionnaireService } from 'app/entities/questionnaire';
import { ICategorie } from 'app/shared/model/categorie.model';
import { CategorieService } from 'app/entities/categorie';

@Component({
    selector: 'jhi-question-update',
    templateUrl: './question-update.component.html'
})
export class QuestionUpdateComponent implements OnInit {
    question: IQuestion;
    isSaving: boolean;

    questionnaires: IQuestionnaire[];

    categories: ICategorie[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected questionService: QuestionService,
        protected questionnaireService: QuestionnaireService,
        protected categorieService: CategorieService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ question }) => {
            this.question = question;
        });
        this.questionnaireService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IQuestionnaire[]>) => mayBeOk.ok),
                map((response: HttpResponse<IQuestionnaire[]>) => response.body)
            )
            .subscribe((res: IQuestionnaire[]) => (this.questionnaires = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.categorieService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICategorie[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICategorie[]>) => response.body)
            )
            .subscribe((res: ICategorie[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.question.id !== undefined) {
            this.subscribeToSaveResponse(this.questionService.update(this.question));
        } else {
            this.subscribeToSaveResponse(this.questionService.create(this.question));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestion>>) {
        result.subscribe((res: HttpResponse<IQuestion>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackQuestionnaireById(index: number, item: IQuestionnaire) {
        return item.id;
    }

    trackCategorieById(index: number, item: ICategorie) {
        return item.id;
    }
}
