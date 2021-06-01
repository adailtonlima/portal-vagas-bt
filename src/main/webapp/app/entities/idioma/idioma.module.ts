import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IdiomaComponent } from './list/idioma.component';
import { IdiomaDetailComponent } from './detail/idioma-detail.component';
import { IdiomaUpdateComponent } from './update/idioma-update.component';
import { IdiomaDeleteDialogComponent } from './delete/idioma-delete-dialog.component';
import { IdiomaRoutingModule } from './route/idioma-routing.module';

@NgModule({
  imports: [SharedModule, IdiomaRoutingModule],
  declarations: [IdiomaComponent, IdiomaDetailComponent, IdiomaUpdateComponent, IdiomaDeleteDialogComponent],
  entryComponents: [IdiomaDeleteDialogComponent],
})
export class IdiomaModule {}
