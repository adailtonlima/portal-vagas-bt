import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIdioma } from '../idioma.model';
import { IdiomaService } from '../service/idioma.service';

@Component({
  templateUrl: './idioma-delete-dialog.component.html',
})
export class IdiomaDeleteDialogComponent {
  idioma?: IIdioma;

  constructor(protected idiomaService: IdiomaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.idiomaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
