import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFuncaoPessoa } from '../funcao-pessoa.model';
import { FuncaoPessoaService } from '../service/funcao-pessoa.service';

@Component({
  templateUrl: './funcao-pessoa-delete-dialog.component.html',
})
export class FuncaoPessoaDeleteDialogComponent {
  funcaoPessoa?: IFuncaoPessoa;

  constructor(protected funcaoPessoaService: FuncaoPessoaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.funcaoPessoaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
