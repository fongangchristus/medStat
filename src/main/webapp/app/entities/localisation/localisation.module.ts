import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MedStatSharedModule } from 'app/shared';
import {
    LocalisationComponent,
    LocalisationDetailComponent,
    LocalisationUpdateComponent,
    LocalisationDeletePopupComponent,
    LocalisationDeleteDialogComponent,
    localisationRoute,
    localisationPopupRoute
} from './';

const ENTITY_STATES = [...localisationRoute, ...localisationPopupRoute];

@NgModule({
    imports: [MedStatSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        LocalisationComponent,
        LocalisationDetailComponent,
        LocalisationUpdateComponent,
        LocalisationDeleteDialogComponent,
        LocalisationDeletePopupComponent
    ],
    entryComponents: [
        LocalisationComponent,
        LocalisationUpdateComponent,
        LocalisationDeleteDialogComponent,
        LocalisationDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MedStatLocalisationModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
