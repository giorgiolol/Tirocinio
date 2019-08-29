import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILibro } from 'app/shared/model/libro.model';
import { LibroService } from './libro.service';

@Component({
  selector: 'jhi-libro-delete-dialog',
  templateUrl: './libro-delete-dialog.component.html'
})
export class LibroDeleteDialogComponent {
  libro: ILibro;

  constructor(protected libroService: LibroService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.libroService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'libroListModification',
        content: 'Deleted an libro'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-libro-delete-popup',
  template: ''
})
export class LibroDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ libro }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(LibroDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.libro = libro;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/libro', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/libro', { outlets: { popup: null } }]);
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
