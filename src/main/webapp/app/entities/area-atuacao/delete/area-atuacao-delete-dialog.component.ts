import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAreaAtuacao } from '../area-atuacao.model';
import { AreaAtuacaoService } from '../service/area-atuacao.service';

@Component({
  templateUrl: './area-atuacao-delete-dialog.component.html',
})
export class AreaAtuacaoDeleteDialogComponent {
  areaAtuacao?: IAreaAtuacao;

  constructor(protected areaAtuacaoService: AreaAtuacaoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.areaAtuacaoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
