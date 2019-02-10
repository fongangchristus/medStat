import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MedStatSharedModule } from 'app/shared';
import {
    MedecinComponent,
    MedecinDetailComponent,
    MedecinUpdateComponent,
    MedecinDeletePopupComponent,
    MedecinDeleteDialogComponent,
    medecinRoute,
    medecinPopupRoute
} from './';

const ENTITY_STATES = [...medecinRoute, ...medecinPopupRoute];

@NgModule({
    imports: [MedStatSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MedecinComponent,
        MedecinDetailComponent,
        MedecinUpdateComponent,
        MedecinDeleteDialogComponent,
        MedecinDeletePopupComponent
    ],
    entryComponents: [MedecinComponent, MedecinUpdateComponent, MedecinDeleteDialogComponent, MedecinDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MedStatMedecinModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
