import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormacao, getFormacaoIdentifier } from '../formacao.model';

export type EntityResponseType = HttpResponse<IFormacao>;
export type EntityArrayResponseType = HttpResponse<IFormacao[]>;

@Injectable({ providedIn: 'root' })
export class FormacaoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/formacaos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(formacao: IFormacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formacao);
    return this.http
      .post<IFormacao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(formacao: IFormacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formacao);
    return this.http
      .put<IFormacao>(`${this.resourceUrl}/${getFormacaoIdentifier(formacao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(formacao: IFormacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formacao);
    return this.http
      .patch<IFormacao>(`${this.resourceUrl}/${getFormacaoIdentifier(formacao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFormacao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFormacao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormacaoToCollectionIfMissing(formacaoCollection: IFormacao[], ...formacaosToCheck: (IFormacao | null | undefined)[]): IFormacao[] {
    const formacaos: IFormacao[] = formacaosToCheck.filter(isPresent);
    if (formacaos.length > 0) {
      const formacaoCollectionIdentifiers = formacaoCollection.map(formacaoItem => getFormacaoIdentifier(formacaoItem)!);
      const formacaosToAdd = formacaos.filter(formacaoItem => {
        const formacaoIdentifier = getFormacaoIdentifier(formacaoItem);
        if (formacaoIdentifier == null || formacaoCollectionIdentifiers.includes(formacaoIdentifier)) {
          return false;
        }
        formacaoCollectionIdentifiers.push(formacaoIdentifier);
        return true;
      });
      return [...formacaosToAdd, ...formacaoCollection];
    }
    return formacaoCollection;
  }

  protected convertDateFromClient(formacao: IFormacao): IFormacao {
    return Object.assign({}, formacao, {
      inicio: formacao.inicio?.isValid() ? formacao.inicio.format(DATE_FORMAT) : undefined,
      conclusao: formacao.conclusao?.isValid() ? formacao.conclusao.format(DATE_FORMAT) : undefined,
      criado: formacao.criado?.isValid() ? formacao.criado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.inicio = res.body.inicio ? dayjs(res.body.inicio) : undefined;
      res.body.conclusao = res.body.conclusao ? dayjs(res.body.conclusao) : undefined;
      res.body.criado = res.body.criado ? dayjs(res.body.criado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((formacao: IFormacao) => {
        formacao.inicio = formacao.inicio ? dayjs(formacao.inicio) : undefined;
        formacao.conclusao = formacao.conclusao ? dayjs(formacao.conclusao) : undefined;
        formacao.criado = formacao.criado ? dayjs(formacao.criado) : undefined;
      });
    }
    return res;
  }
}
