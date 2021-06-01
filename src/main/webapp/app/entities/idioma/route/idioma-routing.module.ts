import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IdiomaComponent } from '../list/idioma.component';
import { IdiomaDetailComponent } from '../detail/idioma-detail.component';
import { IdiomaUpdateComponent } from '../update/idioma-update.component';
import { IdiomaRoutingResolveService } from './idioma-routing-resolve.service';

const idiomaRoute: Routes = [
  {
    path: '',
    component: IdiomaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IdiomaDetailComponent,
    resolve: {
      idioma: IdiomaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IdiomaUpdateComponent,
    resolve: {
      idioma: IdiomaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IdiomaUpdateComponent,
    resolve: {
      idioma: IdiomaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(idiomaRoute)],
  exports: [RouterModule],
})
export class IdiomaRoutingModule {}
