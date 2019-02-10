import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReponse } from 'app/shared/model/reponse.model';

@Component({
    selector: 'jhi-reponse-detail',
    templateUrl: './reponse-detail.component.html'
})
export class ReponseDetailComponent implements OnInit {
    reponse: IReponse;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reponse }) => {
            this.reponse = reponse;
        });
    }

    previousState() {
        window.history.back();
    }
}
