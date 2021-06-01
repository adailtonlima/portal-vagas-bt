import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPerfilProfissional, getPerfilProfissionalIdentifier } from '../perfil-profissional.model';

export type EntityResponseType = HttpResponse<IPerfilProfissional>;
export type EntityArrayResponseType = HttpResponse<IPerfilProfissional[]>;

@Injectable({ providedIn: 'root' })
export class PerfilProfissionalService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/perfil-profissionals');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(perfilProfissional: IPerfilProfissional): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(perfilProfissional);
    return this.http
      .post<IPerfilProfissional>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(perfilProfissional: IPerfilProfissional): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(perfilProfissional);
    return this.http
      .put<IPerfilProfissional>(`${this.resourceUrl}/${getPerfilProfissionalIdentifier(perfilProfissional) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(perfilProfissional: IPerfilProfissional): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(perfilProfissional);
    return this.http
      .patch<IPerfilProfissional>(`${this.resourceUrl}/${getPerfilProfissionalIdentifier(perfilProfissional) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPerfilProfissional>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPerfilProfissional[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPerfilProfissionalToCollectionIfMissing(
    perfilProfissionalCollection: IPerfilProfissional[],
    ...perfilProfissionalsToCheck: (IPerfilProfissional | null | undefined)[]
  ): IPerfilProfissional[] {
    const perfilProfissionals: IPerfilProfissional[] = perfilProfissionalsToCheck.filter(isPresent);
    if (perfilProfissionals.length > 0) {
      const perfilProfissionalCollectionIdentifiers = perfilProfissionalCollection.map(
        perfilProfissionalItem => getPerfilProfissionalIdentifier(perfilProfissionalItem)!
      );
      const perfilProfissionalsToAdd = perfilProfissionals.filter(perfilProfissionalItem => {
        const perfilProfissionalIdentifier = getPerfilProfissionalIdentifier(perfilProfissionalItem);
        if (perfilProfissionalIdentifier == null || perfilProfissionalCollectionIdentifiers.includes(perfilProfissionalIdentifier)) {
          return false;
        }
        perfilProfissionalCollectionIdentifiers.push(perfilProfissionalIdentifier);
        return true;
      });
      return [...perfilProfissionalsToAdd, ...perfilProfissionalCollection];
    }
    return perfilProfissionalCollection;
  }

  protected convertDateFromClient(perfilProfissional: IPerfilProfissional): IPerfilProfissional {
    return Object.assign({}, perfilProfissional, {
      criado: perfilProfissional.criado?.isValid() ? perfilProfissional.criado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.criado = res.body.criado ? dayjs(res.body.criado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((perfilProfissional: IPerfilProfissional) => {
        perfilProfissional.criado = perfilProfissional.criado ? dayjs(perfilProfissional.criado) : undefined;
      });
    }
    return res;
  }
}
