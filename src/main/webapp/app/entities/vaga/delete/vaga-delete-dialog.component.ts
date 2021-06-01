import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVaga } from '../vaga.model';
import { VagaService } from '../service/vaga.service';

@Component({
  templateUrl: './vaga-delete-dialog.component.html',
})
export class VagaDeleteDialogComponent {
  vaga?: IVaga;

  constructor(protected vagaService: VagaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vagaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
