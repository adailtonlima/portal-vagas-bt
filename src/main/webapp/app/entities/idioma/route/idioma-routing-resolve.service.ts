import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIdioma, Idioma } from '../idioma.model';
import { IdiomaService } from '../service/idioma.service';

@Injectable({ providedIn: 'root' })
export class IdiomaRoutingResolveService implements Resolve<IIdioma> {
  constructor(protected service: IdiomaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIdioma> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((idioma: HttpResponse<Idioma>) => {
          if (idioma.body) {
            return of(idioma.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Idioma());
  }
}
