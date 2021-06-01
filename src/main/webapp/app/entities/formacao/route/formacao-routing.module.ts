import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormacaoComponent } from '../list/formacao.component';
import { FormacaoDetailComponent } from '../detail/formacao-detail.component';
import { FormacaoUpdateComponent } from '../update/formacao-update.component';
import { FormacaoRoutingResolveService } from './formacao-routing-resolve.service';

const formacaoRoute: Routes = [
  {
    path: '',
    component: FormacaoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormacaoDetailComponent,
    resolve: {
      formacao: FormacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormacaoUpdateComponent,
    resolve: {
      formacao: FormacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormacaoUpdateComponent,
    resolve: {
      formacao: FormacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formacaoRoute)],
  exports: [RouterModule],
})
export class FormacaoRoutingModule {}
