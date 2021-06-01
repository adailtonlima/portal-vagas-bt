import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PerfilProfissionalComponent } from './list/perfil-profissional.component';
import { PerfilProfissionalDetailComponent } from './detail/perfil-profissional-detail.component';
import { PerfilProfissionalUpdateComponent } from './update/perfil-profissional-update.component';
import { PerfilProfissionalDeleteDialogComponent } from './delete/perfil-profissional-delete-dialog.component';
import { PerfilProfissionalRoutingModule } from './route/perfil-profissional-routing.module';

@NgModule({
  imports: [SharedModule, PerfilProfissionalRoutingModule],
  declarations: [
    PerfilProfissionalComponent,
    PerfilProfissionalDetailComponent,
    PerfilProfissionalUpdateComponent,
    PerfilProfissionalDeleteDialogComponent,
  ],
  entryComponents: [PerfilProfissionalDeleteDialogComponent],
})
export class PerfilProfissionalModule {}
