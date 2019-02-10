import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ICategorie } from 'app/shared/model/categorie.model';
import { CategorieService } from './categorie.service';

@Component({
    selector: 'jhi-categorie-update',
    templateUrl: './categorie-update.component.html'
})
export class CategorieUpdateComponent implements OnInit {
    categorie: ICategorie;
    isSaving: boolean;

    constructor(protected categorieService: CategorieService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ categorie }) => {
            this.categorie = categorie;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.categorie.id !== undefined) {
            this.subscribeToSaveResponse(this.categorieService.update(this.categorie));
        } else {
            this.subscribeToSaveResponse(this.categorieService.create(this.categorie));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategorie>>) {
        result.subscribe((res: HttpResponse<ICategorie>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
