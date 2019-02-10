import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISyntheseEvaluation } from 'app/shared/model/synthese-evaluation.model';
import { SyntheseEvaluationService } from './synthese-evaluation.service';

@Component({
    selector: 'jhi-synthese-evaluation-delete-dialog',
    templateUrl: './synthese-evaluation-delete-dialog.component.html'
})
export class SyntheseEvaluationDeleteDialogComponent {
    syntheseEvaluation: ISyntheseEvaluation;

    constructor(
        protected syntheseEvaluationService: SyntheseEvaluationService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.syntheseEvaluationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'syntheseEvaluationListModification',
                content: 'Deleted an syntheseEvaluation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-synthese-evaluation-delete-popup',
    template: ''
})
export class SyntheseEvaluationDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ syntheseEvaluation }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SyntheseEvaluationDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.syntheseEvaluation = syntheseEvaluation;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/synthese-evaluation', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/synthese-evaluation', { outlets: { popup: null } }]);
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
