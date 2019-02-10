import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IReponse } from 'app/shared/model/reponse.model';
import { ReponseService } from './reponse.service';
import { IQuestion } from 'app/shared/model/question.model';
import { QuestionService } from 'app/entities/question';

@Component({
    selector: 'jhi-reponse-update',
    templateUrl: './reponse-update.component.html'
})
export class ReponseUpdateComponent implements OnInit {
    reponse: IReponse;
    isSaving: boolean;

    questions: IQuestion[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected reponseService: ReponseService,
        protected questionService: QuestionService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reponse }) => {
            this.reponse = reponse;
        });
        this.questionService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IQuestion[]>) => mayBeOk.ok),
                map((response: HttpResponse<IQuestion[]>) => response.body)
            )
            .subscribe((res: IQuestion[]) => (this.questions = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.reponse.id !== undefined) {
            this.subscribeToSaveResponse(this.reponseService.update(this.reponse));
        } else {
            this.subscribeToSaveResponse(this.reponseService.create(this.reponse));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IReponse>>) {
        result.subscribe((res: HttpResponse<IReponse>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackQuestionById(index: number, item: IQuestion) {
        return item.id;
    }
}
