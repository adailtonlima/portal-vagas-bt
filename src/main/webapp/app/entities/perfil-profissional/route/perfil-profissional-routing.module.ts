import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PerfilProfissionalComponent } from '../list/perfil-profissional.component';
import { PerfilProfissionalDetailComponent } from '../detail/perfil-profissional-detail.component';
import { PerfilProfissionalUpdateComponent } from '../update/perfil-profissional-update.component';
import { PerfilProfissionalRoutingResolveService } from './perfil-profissional-routing-resolve.service';

const perfilProfissionalRoute: Routes = [
  {
    path: '',
    component: PerfilProfissionalComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PerfilProfissionalDetailComponent,
    resolve: {
      perfilProfissional: PerfilProfissionalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PerfilProfissionalUpdateComponent,
    resolve: {
      perfilProfissional: PerfilProfissionalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PerfilProfissionalUpdateComponent,
    resolve: {
      perfilProfissional: PerfilProfissionalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(perfilProfissionalRoute)],
  exports: [RouterModule],
})
export class PerfilProfissionalRoutingModule {}
