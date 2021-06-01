import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIdioma, getIdiomaIdentifier } from '../idioma.model';

export type EntityResponseType = HttpResponse<IIdioma>;
export type EntityArrayResponseType = HttpResponse<IIdioma[]>;

@Injectable({ providedIn: 'root' })
export class IdiomaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/idiomas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(idioma: IIdioma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(idioma);
    return this.http
      .post<IIdioma>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(idioma: IIdioma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(idioma);
    return this.http
      .put<IIdioma>(`${this.resourceUrl}/${getIdiomaIdentifier(idioma) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(idioma: IIdioma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(idioma);
    return this.http
      .patch<IIdioma>(`${this.resourceUrl}/${getIdiomaIdentifier(idioma) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIdioma>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIdioma[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIdiomaToCollectionIfMissing(idiomaCollection: IIdioma[], ...idiomasToCheck: (IIdioma | null | undefined)[]): IIdioma[] {
    const idiomas: IIdioma[] = idiomasToCheck.filter(isPresent);
    if (idiomas.length > 0) {
      const idiomaCollectionIdentifiers = idiomaCollection.map(idiomaItem => getIdiomaIdentifier(idiomaItem)!);
      const idiomasToAdd = idiomas.filter(idiomaItem => {
        const idiomaIdentifier = getIdiomaIdentifier(idiomaItem);
        if (idiomaIdentifier == null || idiomaCollectionIdentifiers.includes(idiomaIdentifier)) {
          return false;
        }
        idiomaCollectionIdentifiers.push(idiomaIdentifier);
        return true;
      });
      return [...idiomasToAdd, ...idiomaCollection];
    }
    return idiomaCollection;
  }

  protected convertDateFromClient(idioma: IIdioma): IIdioma {
    return Object.assign({}, idioma, {
      criado: idioma.criado?.isValid() ? idioma.criado.toJSON() : undefined,
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
      res.body.forEach((idioma: IIdioma) => {
        idioma.criado = idioma.criado ? dayjs(idioma.criado) : undefined;
      });
    }
    return res;
  }
}
