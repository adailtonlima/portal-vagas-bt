import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVaga, Vaga } from '../vaga.model';
import { VagaService } from '../service/vaga.service';

@Injectable({ providedIn: 'root' })
export class VagaRoutingResolveService implements Resolve<IVaga> {
  constructor(protected service: VagaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVaga> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vaga: HttpResponse<Vaga>) => {
          if (vaga.body) {
            return of(vaga.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vaga());
  }
}
