import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExperienciaProfissional, getExperienciaProfissionalIdentifier } from '../experiencia-profissional.model';

export type EntityResponseType = HttpResponse<IExperienciaProfissional>;
export type EntityArrayResponseType = HttpResponse<IExperienciaProfissional[]>;

@Injectable({ providedIn: 'root' })
export class ExperienciaProfissionalService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/experiencia-profissionals');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(experienciaProfissional: IExperienciaProfissional): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experienciaProfissional);
    return this.http
      .post<IExperienciaProfissional>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(experienciaProfissional: IExperienciaProfissional): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experienciaProfissional);
    return this.http
      .put<IExperienciaProfissional>(
        `${this.resourceUrl}/${getExperienciaProfissionalIdentifier(experienciaProfissional) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(experienciaProfissional: IExperienciaProfissional): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experienciaProfissional);
    return this.http
      .patch<IExperienciaProfissional>(
        `${this.resourceUrl}/${getExperienciaProfissionalIdentifier(experienciaProfissional) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExperienciaProfissional>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExperienciaProfissional[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExperienciaProfissionalToCollectionIfMissing(
    experienciaProfissionalCollection: IExperienciaProfissional[],
    ...experienciaProfissionalsToCheck: (IExperienciaProfissional | null | undefined)[]
  ): IExperienciaProfissional[] {
    const experienciaProfissionals: IExperienciaProfissional[] = experienciaProfissionalsToCheck.filter(isPresent);
    if (experienciaProfissionals.length > 0) {
      const experienciaProfissionalCollectionIdentifiers = experienciaProfissionalCollection.map(
        experienciaProfissionalItem => getExperienciaProfissionalIdentifier(experienciaProfissionalItem)!
      );
      const experienciaProfissionalsToAdd = experienciaProfissionals.filter(experienciaProfissionalItem => {
        const experienciaProfissionalIdentifier = getExperienciaProfissionalIdentifier(experienciaProfissionalItem);
        if (
          experienciaProfissionalIdentifier == null ||
          experienciaProfissionalCollectionIdentifiers.includes(experienciaProfissionalIdentifier)
        ) {
          return false;
        }
        experienciaProfissionalCollectionIdentifiers.push(experienciaProfissionalIdentifier);
        return true;
      });
      return [...experienciaProfissionalsToAdd, ...experienciaProfissionalCollection];
    }
    return experienciaProfissionalCollection;
  }

  protected convertDateFromClient(experienciaProfissional: IExperienciaProfissional): IExperienciaProfissional {
    return Object.assign({}, experienciaProfissional, {
      inicio: experienciaProfissional.inicio?.isValid() ? experienciaProfissional.inicio.format(DATE_FORMAT) : undefined,
      fim: experienciaProfissional.fim?.isValid() ? experienciaProfissional.fim.format(DATE_FORMAT) : undefined,
      criado: experienciaProfissional.criado?.isValid() ? experienciaProfissional.criado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.inicio = res.body.inicio ? dayjs(res.body.inicio) : undefined;
      res.body.fim = res.body.fim ? dayjs(res.body.fim) : undefined;
      res.body.criado = res.body.criado ? dayjs(res.body.criado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((experienciaProfissional: IExperienciaProfissional) => {
        experienciaProfissional.inicio = experienciaProfissional.inicio ? dayjs(experienciaProfissional.inicio) : undefined;
        experienciaProfissional.fim = experienciaProfissional.fim ? dayjs(experienciaProfissional.fim) : undefined;
        experienciaProfissional.criado = experienciaProfissional.criado ? dayjs(experienciaProfissional.criado) : undefined;
      });
    }
    return res;
  }
}
