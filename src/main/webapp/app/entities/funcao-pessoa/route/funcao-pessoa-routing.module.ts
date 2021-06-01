import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FuncaoPessoaComponent } from '../list/funcao-pessoa.component';
import { FuncaoPessoaDetailComponent } from '../detail/funcao-pessoa-detail.component';
import { FuncaoPessoaUpdateComponent } from '../update/funcao-pessoa-update.component';
import { FuncaoPessoaRoutingResolveService } from './funcao-pessoa-routing-resolve.service';

const funcaoPessoaRoute: Routes = [
  {
    path: '',
    component: FuncaoPessoaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FuncaoPessoaDetailComponent,
    resolve: {
      funcaoPessoa: FuncaoPessoaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FuncaoPessoaUpdateComponent,
    resolve: {
      funcaoPessoa: FuncaoPessoaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FuncaoPessoaUpdateComponent,
    resolve: {
      funcaoPessoa: FuncaoPessoaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(funcaoPessoaRoute)],
  exports: [RouterModule],
})
export class FuncaoPessoaRoutingModule {}
