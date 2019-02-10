import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISyntheseEvaluation } from 'app/shared/model/synthese-evaluation.model';

@Component({
    selector: 'jhi-synthese-evaluation-detail',
    templateUrl: './synthese-evaluation-detail.component.html'
})
export class SyntheseEvaluationDetailComponent implements OnInit {
    syntheseEvaluation: ISyntheseEvaluation;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ syntheseEvaluation }) => {
            this.syntheseEvaluation = syntheseEvaluation;
        });
    }

    previousState() {
        window.history.back();
    }
}
