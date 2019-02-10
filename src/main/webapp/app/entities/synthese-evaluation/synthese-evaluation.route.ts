import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SyntheseEvaluation } from 'app/shared/model/synthese-evaluation.model';
import { SyntheseEvaluationService } from './synthese-evaluation.service';
import { SyntheseEvaluationComponent } from './synthese-evaluation.component';
import { SyntheseEvaluationDetailComponent } from './synthese-evaluation-detail.component';
import { SyntheseEvaluationUpdateComponent } from './synthese-evaluation-update.component';
import { SyntheseEvaluationDeletePopupComponent } from './synthese-evaluation-delete-dialog.component';
import { ISyntheseEvaluation } from 'app/shared/model/synthese-evaluation.model';

@Injectable({ providedIn: 'root' })
export class SyntheseEvaluationResolve implements Resolve<ISyntheseEvaluation> {
    constructor(private service: SyntheseEvaluationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISyntheseEvaluation> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SyntheseEvaluation>) => response.ok),
                map((syntheseEvaluation: HttpResponse<SyntheseEvaluation>) => syntheseEvaluation.body)
            );
        }
        return of(new SyntheseEvaluation());
    }
}

export const syntheseEvaluationRoute: Routes = [
    {
        path: '',
        component: SyntheseEvaluationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'medStatApp.syntheseEvaluation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SyntheseEvaluationDetailComponent,
        resolve: {
            syntheseEvaluation: SyntheseEvaluationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.syntheseEvaluation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SyntheseEvaluationUpdateComponent,
        resolve: {
            syntheseEvaluation: SyntheseEvaluationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.syntheseEvaluation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SyntheseEvaluationUpdateComponent,
        resolve: {
            syntheseEvaluation: SyntheseEvaluationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.syntheseEvaluation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const syntheseEvaluationPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SyntheseEvaluationDeletePopupComponent,
        resolve: {
            syntheseEvaluation: SyntheseEvaluationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.syntheseEvaluation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
