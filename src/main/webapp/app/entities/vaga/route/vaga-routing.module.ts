import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VagaComponent } from '../list/vaga.component';
import { VagaDetailComponent } from '../detail/vaga-detail.component';
import { VagaUpdateComponent } from '../update/vaga-update.component';
import { VagaRoutingResolveService } from './vaga-routing-resolve.service';

const vagaRoute: Routes = [
  {
    path: '',
    component: VagaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VagaDetailComponent,
    resolve: {
      vaga: VagaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VagaUpdateComponent,
    resolve: {
      vaga: VagaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VagaUpdateComponent,
    resolve: {
      vaga: VagaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vagaRoute)],
  exports: [RouterModule],
})
export class VagaRoutingModule {}
