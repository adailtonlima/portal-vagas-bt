import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExperienciaProfissionalComponent } from '../list/experiencia-profissional.component';
import { ExperienciaProfissionalDetailComponent } from '../detail/experiencia-profissional-detail.component';
import { ExperienciaProfissionalUpdateComponent } from '../update/experiencia-profissional-update.component';
import { ExperienciaProfissionalRoutingResolveService } from './experiencia-profissional-routing-resolve.service';

const experienciaProfissionalRoute: Routes = [
  {
    path: '',
    component: ExperienciaProfissionalComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExperienciaProfissionalDetailComponent,
    resolve: {
      experienciaProfissional: ExperienciaProfissionalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExperienciaProfissionalUpdateComponent,
    resolve: {
      experienciaProfissional: ExperienciaProfissionalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExperienciaProfissionalUpdateComponent,
    resolve: {
      experienciaProfissional: ExperienciaProfissionalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(experienciaProfissionalRoute)],
  exports: [RouterModule],
})
export class ExperienciaProfissionalRoutingModule {}
