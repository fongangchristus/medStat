import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMedecin } from 'app/shared/model/medecin.model';
import { MedecinService } from './medecin.service';

@Component({
    selector: 'jhi-medecin-delete-dialog',
    templateUrl: './medecin-delete-dialog.component.html'
})
export class MedecinDeleteDialogComponent {
    medecin: IMedecin;

    constructor(protected medecinService: MedecinService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.medecinService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'medecinListModification',
                content: 'Deleted an medecin'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-medecin-delete-popup',
    template: ''
})
export class MedecinDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ medecin }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MedecinDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.medecin = medecin;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/medecin', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/medecin', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
