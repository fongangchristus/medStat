import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReponse } from 'app/shared/model/reponse.model';
import { ReponseService } from './reponse.service';

@Component({
    selector: 'jhi-reponse-delete-dialog',
    templateUrl: './reponse-delete-dialog.component.html'
})
export class ReponseDeleteDialogComponent {
    reponse: IReponse;

    constructor(protected reponseService: ReponseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reponseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'reponseListModification',
                content: 'Deleted an reponse'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-reponse-delete-popup',
    template: ''
})
export class ReponseDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reponse }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ReponseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.reponse = reponse;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/reponse', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/reponse', { outlets: { popup: null } }]);
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
