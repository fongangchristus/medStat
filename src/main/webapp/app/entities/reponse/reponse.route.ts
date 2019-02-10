import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Reponse } from 'app/shared/model/reponse.model';
import { ReponseService } from './reponse.service';
import { ReponseComponent } from './reponse.component';
import { ReponseDetailComponent } from './reponse-detail.component';
import { ReponseUpdateComponent } from './reponse-update.component';
import { ReponseDeletePopupComponent } from './reponse-delete-dialog.component';
import { IReponse } from 'app/shared/model/reponse.model';

@Injectable({ providedIn: 'root' })
export class ReponseResolve implements Resolve<IReponse> {
    constructor(private service: ReponseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IReponse> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Reponse>) => response.ok),
                map((reponse: HttpResponse<Reponse>) => reponse.body)
            );
        }
        return of(new Reponse());
    }
}

export const reponseRoute: Routes = [
    {
        path: '',
        component: ReponseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'medStatApp.reponse.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ReponseDetailComponent,
        resolve: {
            reponse: ReponseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.reponse.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ReponseUpdateComponent,
        resolve: {
            reponse: ReponseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.reponse.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ReponseUpdateComponent,
        resolve: {
            reponse: ReponseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.reponse.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reponsePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ReponseDeletePopupComponent,
        resolve: {
            reponse: ReponseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.reponse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
