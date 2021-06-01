import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AreaAtuacaoComponent } from './list/area-atuacao.component';
import { AreaAtuacaoDetailComponent } from './detail/area-atuacao-detail.component';
import { AreaAtuacaoUpdateComponent } from './update/area-atuacao-update.component';
import { AreaAtuacaoDeleteDialogComponent } from './delete/area-atuacao-delete-dialog.component';
import { AreaAtuacaoRoutingModule } from './route/area-atuacao-routing.module';

@NgModule({
  imports: [SharedModule, AreaAtuacaoRoutingModule],
  declarations: [AreaAtuacaoComponent, AreaAtuacaoDetailComponent, AreaAtuacaoUpdateComponent, AreaAtuacaoDeleteDialogComponent],
  entryComponents: [AreaAtuacaoDeleteDialogComponent],
})
export class AreaAtuacaoModule {}
