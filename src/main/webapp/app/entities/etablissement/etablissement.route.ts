import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Etablissement } from 'app/shared/model/etablissement.model';
import { EtablissementService } from './etablissement.service';
import { EtablissementComponent } from './etablissement.component';
import { EtablissementDetailComponent } from './etablissement-detail.component';
import { EtablissementUpdateComponent } from './etablissement-update.component';
import { EtablissementDeletePopupComponent } from './etablissement-delete-dialog.component';
import { IEtablissement } from 'app/shared/model/etablissement.model';

@Injectable({ providedIn: 'root' })
export class EtablissementResolve implements Resolve<IEtablissement> {
    constructor(private service: EtablissementService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEtablissement> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Etablissement>) => response.ok),
                map((etablissement: HttpResponse<Etablissement>) => etablissement.body)
            );
        }
        return of(new Etablissement());
    }
}

export const etablissementRoute: Routes = [
    {
        path: '',
        component: EtablissementComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'medStatApp.etablissement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: EtablissementDetailComponent,
        resolve: {
            etablissement: EtablissementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.etablissement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: EtablissementUpdateComponent,
        resolve: {
            etablissement: EtablissementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.etablissement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: EtablissementUpdateComponent,
        resolve: {
            etablissement: EtablissementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.etablissement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const etablissementPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: EtablissementDeletePopupComponent,
        resolve: {
            etablissement: EtablissementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'medStatApp.etablissement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
