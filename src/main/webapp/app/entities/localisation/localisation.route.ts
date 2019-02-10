import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Localisation } from 'app/shared/model/localisation.model';
import { LocalisationService } from './localisation.service';
import { LocalisationComponent } from './localisation.component';
import { LocalisationDetailComponent } from './localisation-detail.component';
import { LocalisationUpdateComponent } from './localisation-update.component';
import { LocalisationDeletePopupComponent } from './localisation-delete-dialog.component';
import { ILocalisation } from 'app/shared/model/localisation.model';

@Injectable({ providedIn: 'root' })
export class LocalisationResolve implements Resolve<ILocalisation> {
    constructor(private service: LocalisationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILocalisation> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Localisation>) => response.ok),
                map((localisation: HttpResponse<Localisation>) => localisation.body)
            );
        }
        return of(new Localisation());
    }
}

export const localisationRoute: Routes = [
    {
        path: '',
        component: LocalisationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'medStatApp.localisation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: LocalisationDetailComponent,
        resolve: {
            localisation: LocalisationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.localisation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: LocalisationUpdateComponent,
        resolve: {
            localisation: LocalisationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.localisation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: LocalisationUpdateComponent,
        resolve: {
            localisation: LocalisationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.localisation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const localisationPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: LocalisationDeletePopupComponent,
        resolve: {
            localisation: LocalisationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.localisation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
