import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVaga, getVagaIdentifier } from '../vaga.model';

export type EntityResponseType = HttpResponse<IVaga>;
export type EntityArrayResponseType = HttpResponse<IVaga[]>;

@Injectable({ providedIn: 'root' })
export class VagaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/vagas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(vaga: IVaga): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaga);
    return this.http
      .post<IVaga>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vaga: IVaga): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaga);
    return this.http
      .put<IVaga>(`${this.resourceUrl}/${getVagaIdentifier(vaga) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vaga: IVaga): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaga);
    return this.http
      .patch<IVaga>(`${this.resourceUrl}/${getVagaIdentifier(vaga) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVaga>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVaga[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVagaToCollectionIfMissing(vagaCollection: IVaga[], ...vagasToCheck: (IVaga | null | undefined)[]): IVaga[] {
    const vagas: IVaga[] = vagasToCheck.filter(isPresent);
    if (vagas.length > 0) {
      const vagaCollectionIdentifiers = vagaCollection.map(vagaItem => getVagaIdentifier(vagaItem)!);
      const vagasToAdd = vagas.filter(vagaItem => {
        const vagaIdentifier = getVagaIdentifier(vagaItem);
        if (vagaIdentifier == null || vagaCollectionIdentifiers.includes(vagaIdentifier)) {
          return false;
        }
        vagaCollectionIdentifiers.push(vagaIdentifier);
        return true;
      });
      return [...vagasToAdd, ...vagaCollection];
    }
    return vagaCollection;
  }

  protected convertDateFromClient(vaga: IVaga): IVaga {
    return Object.assign({}, vaga, {
      criado: vaga.criado?.isValid() ? vaga.criado.toJSON() : undefined,
      prazoAnuncio: vaga.prazoAnuncio?.isValid() ? vaga.prazoAnuncio.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.criado = res.body.criado ? dayjs(res.body.criado) : undefined;
      res.body.prazoAnuncio = res.body.prazoAnuncio ? dayjs(res.body.prazoAnuncio) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vaga: IVaga) => {
        vaga.criado = vaga.criado ? dayjs(vaga.criado) : undefined;
        vaga.prazoAnuncio = vaga.prazoAnuncio ? dayjs(vaga.prazoAnuncio) : undefined;
      });
    }
    return res;
  }
}
