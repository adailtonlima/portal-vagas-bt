import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFuncaoPessoa, getFuncaoPessoaIdentifier } from '../funcao-pessoa.model';

export type EntityResponseType = HttpResponse<IFuncaoPessoa>;
export type EntityArrayResponseType = HttpResponse<IFuncaoPessoa[]>;

@Injectable({ providedIn: 'root' })
export class FuncaoPessoaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/funcao-pessoas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(funcaoPessoa: IFuncaoPessoa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcaoPessoa);
    return this.http
      .post<IFuncaoPessoa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(funcaoPessoa: IFuncaoPessoa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcaoPessoa);
    return this.http
      .put<IFuncaoPessoa>(`${this.resourceUrl}/${getFuncaoPessoaIdentifier(funcaoPessoa) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(funcaoPessoa: IFuncaoPessoa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcaoPessoa);
    return this.http
      .patch<IFuncaoPessoa>(`${this.resourceUrl}/${getFuncaoPessoaIdentifier(funcaoPessoa) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFuncaoPessoa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFuncaoPessoa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFuncaoPessoaToCollectionIfMissing(
    funcaoPessoaCollection: IFuncaoPessoa[],
    ...funcaoPessoasToCheck: (IFuncaoPessoa | null | undefined)[]
  ): IFuncaoPessoa[] {
    const funcaoPessoas: IFuncaoPessoa[] = funcaoPessoasToCheck.filter(isPresent);
    if (funcaoPessoas.length > 0) {
      const funcaoPessoaCollectionIdentifiers = funcaoPessoaCollection.map(
        funcaoPessoaItem => getFuncaoPessoaIdentifier(funcaoPessoaItem)!
      );
      const funcaoPessoasToAdd = funcaoPessoas.filter(funcaoPessoaItem => {
        const funcaoPessoaIdentifier = getFuncaoPessoaIdentifier(funcaoPessoaItem);
        if (funcaoPessoaIdentifier == null || funcaoPessoaCollectionIdentifiers.includes(funcaoPessoaIdentifier)) {
          return false;
        }
        funcaoPessoaCollectionIdentifiers.push(funcaoPessoaIdentifier);
        return true;
      });
      return [...funcaoPessoasToAdd, ...funcaoPessoaCollection];
    }
    return funcaoPessoaCollection;
  }

  protected convertDateFromClient(funcaoPessoa: IFuncaoPessoa): IFuncaoPessoa {
    return Object.assign({}, funcaoPessoa, {
      criado: funcaoPessoa.criado?.isValid() ? funcaoPessoa.criado.toJSON() : undefined,
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
      res.body.forEach((funcaoPessoa: IFuncaoPessoa) => {
        funcaoPessoa.criado = funcaoPessoa.criado ? dayjs(funcaoPessoa.criado) : undefined;
      });
    }
    return res;
  }
}
