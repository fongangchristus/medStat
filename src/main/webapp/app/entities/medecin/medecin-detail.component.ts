import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMedecin } from 'app/shared/model/medecin.model';

@Component({
    selector: 'jhi-medecin-detail',
    templateUrl: './medecin-detail.component.html'
})
export class MedecinDetailComponent implements OnInit {
    medecin: IMedecin;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ medecin }) => {
            this.medecin = medecin;
        });
    }

    previousState() {
        window.history.back();
    }
}
