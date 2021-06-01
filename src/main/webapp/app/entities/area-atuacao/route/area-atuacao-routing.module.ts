import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AreaAtuacaoComponent } from '../list/area-atuacao.component';
import { AreaAtuacaoDetailComponent } from '../detail/area-atuacao-detail.component';
import { AreaAtuacaoUpdateComponent } from '../update/area-atuacao-update.component';
import { AreaAtuacaoRoutingResolveService } from './area-atuacao-routing-resolve.service';

const areaAtuacaoRoute: Routes = [
  {
    path: '',
    component: AreaAtuacaoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AreaAtuacaoDetailComponent,
    resolve: {
      areaAtuacao: AreaAtuacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AreaAtuacaoUpdateComponent,
    resolve: {
      areaAtuacao: AreaAtuacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AreaAtuacaoUpdateComponent,
    resolve: {
      areaAtuacao: AreaAtuacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(areaAtuacaoRoute)],
  exports: [RouterModule],
})
export class AreaAtuacaoRoutingModule {}
