import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPerfilProfissional } from '../perfil-profissional.model';
import { PerfilProfissionalService } from '../service/perfil-profissional.service';

@Component({
  templateUrl: './perfil-profissional-delete-dialog.component.html',
})
export class PerfilProfissionalDeleteDialogComponent {
  perfilProfissional?: IPerfilProfissional;

  constructor(protected perfilProfissionalService: PerfilProfissionalService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.perfilProfissionalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
