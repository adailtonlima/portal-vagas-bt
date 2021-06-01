import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPerfilProfissional, PerfilProfissional } from '../perfil-profissional.model';
import { PerfilProfissionalService } from '../service/perfil-profissional.service';

@Injectable({ providedIn: 'root' })
export class PerfilProfissionalRoutingResolveService implements Resolve<IPerfilProfissional> {
  constructor(protected service: PerfilProfissionalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPerfilProfissional> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((perfilProfissional: HttpResponse<PerfilProfissional>) => {
          if (perfilProfissional.body) {
            return of(perfilProfissional.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PerfilProfissional());
  }
}
