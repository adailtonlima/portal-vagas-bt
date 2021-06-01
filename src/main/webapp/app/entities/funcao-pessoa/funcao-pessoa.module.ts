import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FuncaoPessoaComponent } from './list/funcao-pessoa.component';
import { FuncaoPessoaDetailComponent } from './detail/funcao-pessoa-detail.component';
import { FuncaoPessoaUpdateComponent } from './update/funcao-pessoa-update.component';
import { FuncaoPessoaDeleteDialogComponent } from './delete/funcao-pessoa-delete-dialog.component';
import { FuncaoPessoaRoutingModule } from './route/funcao-pessoa-routing.module';

@NgModule({
  imports: [SharedModule, FuncaoPessoaRoutingModule],
  declarations: [FuncaoPessoaComponent, FuncaoPessoaDetailComponent, FuncaoPessoaUpdateComponent, FuncaoPessoaDeleteDialogComponent],
  entryComponents: [FuncaoPessoaDeleteDialogComponent],
})
export class FuncaoPessoaModule {}
