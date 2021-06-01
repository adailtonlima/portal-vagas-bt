import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAreaAtuacao, getAreaAtuacaoIdentifier } from '../area-atuacao.model';

export type EntityResponseType = HttpResponse<IAreaAtuacao>;
export type EntityArrayResponseType = HttpResponse<IAreaAtuacao[]>;

@Injectable({ providedIn: 'root' })
export class AreaAtuacaoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/area-atuacaos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(areaAtuacao: IAreaAtuacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(areaAtuacao);
    return this.http
      .post<IAreaAtuacao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(areaAtuacao: IAreaAtuacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(areaAtuacao);
    return this.http
      .put<IAreaAtuacao>(`${this.resourceUrl}/${getAreaAtuacaoIdentifier(areaAtuacao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(areaAtuacao: IAreaAtuacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(areaAtuacao);
    return this.http
      .patch<IAreaAtuacao>(`${this.resourceUrl}/${getAreaAtuacaoIdentifier(areaAtuacao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAreaAtuacao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAreaAtuacao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAreaAtuacaoToCollectionIfMissing(
    areaAtuacaoCollection: IAreaAtuacao[],
    ...areaAtuacaosToCheck: (IAreaAtuacao | null | undefined)[]
  ): IAreaAtuacao[] {
    const areaAtuacaos: IAreaAtuacao[] = areaAtuacaosToCheck.filter(isPresent);
    if (areaAtuacaos.length > 0) {
      const areaAtuacaoCollectionIdentifiers = areaAtuacaoCollection.map(areaAtuacaoItem => getAreaAtuacaoIdentifier(areaAtuacaoItem)!);
      const areaAtuacaosToAdd = areaAtuacaos.filter(areaAtuacaoItem => {
        const areaAtuacaoIdentifier = getAreaAtuacaoIdentifier(areaAtuacaoItem);
        if (areaAtuacaoIdentifier == null || areaAtuacaoCollectionIdentifiers.includes(areaAtuacaoIdentifier)) {
          return false;
        }
        areaAtuacaoCollectionIdentifiers.push(areaAtuacaoIdentifier);
        return true;
      });
      return [...areaAtuacaosToAdd, ...areaAtuacaoCollection];
    }
    return areaAtuacaoCollection;
  }

  protected convertDateFromClient(areaAtuacao: IAreaAtuacao): IAreaAtuacao {
    return Object.assign({}, areaAtuacao, {
      criado: areaAtuacao.criado?.isValid() ? areaAtuacao.criado.toJSON() : undefined,
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
      res.body.forEach((areaAtuacao: IAreaAtuacao) => {
        areaAtuacao.criado = areaAtuacao.criado ? dayjs(areaAtuacao.criado) : undefined;
      });
    }
    return res;
  }
}
