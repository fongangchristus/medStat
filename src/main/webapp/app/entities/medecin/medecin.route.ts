import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Medecin } from 'app/shared/model/medecin.model';
import { MedecinService } from './medecin.service';
import { MedecinComponent } from './medecin.component';
import { MedecinDetailComponent } from './medecin-detail.component';
import { MedecinUpdateComponent } from './medecin-update.component';
import { MedecinDeletePopupComponent } from './medecin-delete-dialog.component';
import { IMedecin } from 'app/shared/model/medecin.model';

@Injectable({ providedIn: 'root' })
export class MedecinResolve implements Resolve<IMedecin> {
    constructor(private service: MedecinService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMedecin> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Medecin>) => response.ok),
                map((medecin: HttpResponse<Medecin>) => medecin.body)
            );
        }
        return of(new Medecin());
    }
}

export const medecinRoute: Routes = [
    {
        path: '',
        component: MedecinComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'medStatApp.medecin.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MedecinDetailComponent,
        resolve: {
            medecin: MedecinResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.medecin.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MedecinUpdateComponent,
        resolve: {
            medecin: MedecinResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.medecin.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: MedecinUpdateComponent,
        resolve: {
            medecin: MedecinResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.medecin.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const medecinPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MedecinDeletePopupComponent,
        resolve: {
            medecin: MedecinResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.medecin.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
