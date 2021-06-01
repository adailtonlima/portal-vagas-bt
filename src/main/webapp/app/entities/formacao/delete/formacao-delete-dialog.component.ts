import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormacao } from '../formacao.model';
import { FormacaoService } from '../service/formacao.service';

@Component({
  templateUrl: './formacao-delete-dialog.component.html',
})
export class FormacaoDeleteDialogComponent {
  formacao?: IFormacao;

  constructor(protected formacaoService: FormacaoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formacaoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
