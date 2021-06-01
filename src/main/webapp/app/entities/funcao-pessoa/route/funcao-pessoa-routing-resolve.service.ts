import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFuncaoPessoa, FuncaoPessoa } from '../funcao-pessoa.model';
import { FuncaoPessoaService } from '../service/funcao-pessoa.service';

@Injectable({ providedIn: 'root' })
export class FuncaoPessoaRoutingResolveService implements Resolve<IFuncaoPessoa> {
  constructor(protected service: FuncaoPessoaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFuncaoPessoa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((funcaoPessoa: HttpResponse<FuncaoPessoa>) => {
          if (funcaoPessoa.body) {
            return of(funcaoPessoa.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FuncaoPessoa());
  }
}
