import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ExperienciaProfissionalComponent } from './list/experiencia-profissional.component';
import { ExperienciaProfissionalDetailComponent } from './detail/experiencia-profissional-detail.component';
import { ExperienciaProfissionalUpdateComponent } from './update/experiencia-profissional-update.component';
import { ExperienciaProfissionalDeleteDialogComponent } from './delete/experiencia-profissional-delete-dialog.component';
import { ExperienciaProfissionalRoutingModule } from './route/experiencia-profissional-routing.module';

@NgModule({
  imports: [SharedModule, ExperienciaProfissionalRoutingModule],
  declarations: [
    ExperienciaProfissionalComponent,
    ExperienciaProfissionalDetailComponent,
    ExperienciaProfissionalUpdateComponent,
    ExperienciaProfissionalDeleteDialogComponent,
  ],
  entryComponents: [ExperienciaProfissionalDeleteDialogComponent],
})
export class ExperienciaProfissionalModule {}
