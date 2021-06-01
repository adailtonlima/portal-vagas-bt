import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { VagaComponent } from './list/vaga.component';
import { VagaDetailComponent } from './detail/vaga-detail.component';
import { VagaUpdateComponent } from './update/vaga-update.component';
import { VagaDeleteDialogComponent } from './delete/vaga-delete-dialog.component';
import { VagaRoutingModule } from './route/vaga-routing.module';

@NgModule({
  imports: [SharedModule, VagaRoutingModule],
  declarations: [VagaComponent, VagaDetailComponent, VagaUpdateComponent, VagaDeleteDialogComponent],
  entryComponents: [VagaDeleteDialogComponent],
})
export class VagaModule {}
