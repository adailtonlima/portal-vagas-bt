import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAreaAtuacao, AreaAtuacao } from '../area-atuacao.model';
import { AreaAtuacaoService } from '../service/area-atuacao.service';

@Injectable({ providedIn: 'root' })
export class AreaAtuacaoRoutingResolveService implements Resolve<IAreaAtuacao> {
  constructor(protected service: AreaAtuacaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAreaAtuacao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((areaAtuacao: HttpResponse<AreaAtuacao>) => {
          if (areaAtuacao.body) {
            return of(areaAtuacao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AreaAtuacao());
  }
}
