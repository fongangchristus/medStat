import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MedStatSharedModule } from 'app/shared';
import {
    QuestionnaireComponent,
    QuestionnaireDetailComponent,
    QuestionnaireUpdateComponent,
    QuestionnaireDeletePopupComponent,
    QuestionnaireDeleteDialogComponent,
    questionnaireRoute,
    questionnairePopupRoute
} from './';

const ENTITY_STATES = [...questionnaireRoute, ...questionnairePopupRoute];

@NgModule({
    imports: [MedStatSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        QuestionnaireComponent,
        QuestionnaireDetailComponent,
        QuestionnaireUpdateComponent,
        QuestionnaireDeleteDialogComponent,
        QuestionnaireDeletePopupComponent
    ],
    entryComponents: [
        QuestionnaireComponent,
        QuestionnaireUpdateComponent,
        QuestionnaireDeleteDialogComponent,
        QuestionnaireDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MedStatQuestionnaireModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
