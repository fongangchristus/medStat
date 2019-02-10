import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MedStatSharedModule } from 'app/shared';
import {
    SyntheseEvaluationComponent,
    SyntheseEvaluationDetailComponent,
    SyntheseEvaluationUpdateComponent,
    SyntheseEvaluationDeletePopupComponent,
    SyntheseEvaluationDeleteDialogComponent,
    syntheseEvaluationRoute,
    syntheseEvaluationPopupRoute
} from './';

const ENTITY_STATES = [...syntheseEvaluationRoute, ...syntheseEvaluationPopupRoute];

@NgModule({
    imports: [MedStatSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SyntheseEvaluationComponent,
        SyntheseEvaluationDetailComponent,
        SyntheseEvaluationUpdateComponent,
        SyntheseEvaluationDeleteDialogComponent,
        SyntheseEvaluationDeletePopupComponent
    ],
    entryComponents: [
        SyntheseEvaluationComponent,
        SyntheseEvaluationUpdateComponent,
        SyntheseEvaluationDeleteDialogComponent,
        SyntheseEvaluationDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MedStatSyntheseEvaluationModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
