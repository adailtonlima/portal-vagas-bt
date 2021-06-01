import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExperienciaProfissional } from '../experiencia-profissional.model';
import { ExperienciaProfissionalService } from '../service/experiencia-profissional.service';

@Component({
  templateUrl: './experiencia-profissional-delete-dialog.component.html',
})
export class ExperienciaProfissionalDeleteDialogComponent {
  experienciaProfissional?: IExperienciaProfissional;

  constructor(protected experienciaProfissionalService: ExperienciaProfissionalService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.experienciaProfissionalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
