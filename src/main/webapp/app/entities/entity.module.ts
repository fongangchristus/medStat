import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'etablissement',
                loadChildren: './etablissement/etablissement.module#MedStatEtablissementModule'
            },
            {
                path: 'localisation',
                loadChildren: './localisation/localisation.module#MedStatLocalisationModule'
            },
            {
                path: 'medecin',
                loadChildren: './medecin/medecin.module#MedStatMedecinModule'
            },
            {
                path: 'evaluation',
                loadChildren: './evaluation/evaluation.module#MedStatEvaluationModule'
            },
            {
                path: 'questionnaire',
                loadChildren: './questionnaire/questionnaire.module#MedStatQuestionnaireModule'
            },
            {
                path: 'question',
                loadChildren: './question/question.module#MedStatQuestionModule'
            },
            {
                path: 'categorie',
                loadChildren: './categorie/categorie.module#MedStatCategorieModule'
            },
            {
                path: 'reponse',
                loadChildren: './reponse/reponse.module#MedStatReponseModule'
            },
            {
                path: 'synthese-evaluation',
                loadChildren: './synthese-evaluation/synthese-evaluation.module#MedStatSyntheseEvaluationModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MedStatEntityModule {}
