import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FormacaoComponent } from './list/formacao.component';
import { FormacaoDetailComponent } from './detail/formacao-detail.component';
import { FormacaoUpdateComponent } from './update/formacao-update.component';
import { FormacaoDeleteDialogComponent } from './delete/formacao-delete-dialog.component';
import { FormacaoRoutingModule } from './route/formacao-routing.module';

@NgModule({
  imports: [SharedModule, FormacaoRoutingModule],
  declarations: [FormacaoComponent, FormacaoDetailComponent, FormacaoUpdateComponent, FormacaoDeleteDialogComponent],
  entryComponents: [FormacaoDeleteDialogComponent],
})
export class FormacaoModule {}
