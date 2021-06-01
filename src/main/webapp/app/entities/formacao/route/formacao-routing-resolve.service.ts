import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormacao, Formacao } from '../formacao.model';
import { FormacaoService } from '../service/formacao.service';

@Injectable({ providedIn: 'root' })
export class FormacaoRoutingResolveService implements Resolve<IFormacao> {
  constructor(protected service: FormacaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormacao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((formacao: HttpResponse<Formacao>) => {
          if (formacao.body) {
            return of(formacao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Formacao());
  }
}
