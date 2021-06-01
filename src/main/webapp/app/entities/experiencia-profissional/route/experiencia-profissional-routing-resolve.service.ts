import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExperienciaProfissional, ExperienciaProfissional } from '../experiencia-profissional.model';
import { ExperienciaProfissionalService } from '../service/experiencia-profissional.service';

@Injectable({ providedIn: 'root' })
export class ExperienciaProfissionalRoutingResolveService implements Resolve<IExperienciaProfissional> {
  constructor(protected service: ExperienciaProfissionalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExperienciaProfissional> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((experienciaProfissional: HttpResponse<ExperienciaProfissional>) => {
          if (experienciaProfissional.body) {
            return of(experienciaProfissional.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExperienciaProfissional());
  }
}
