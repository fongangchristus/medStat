import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEtablissement } from 'app/shared/model/etablissement.model';

@Component({
    selector: 'jhi-etablissement-detail',
    templateUrl: './etablissement-detail.component.html'
})
export class EtablissementDetailComponent implements OnInit {
    etablissement: IEtablissement;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ etablissement }) => {
            this.etablissement = etablissement;
        });
    }

    previousState() {
        window.history.back();
    }
}
