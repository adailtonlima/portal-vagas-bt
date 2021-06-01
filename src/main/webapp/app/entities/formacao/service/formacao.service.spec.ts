import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFormacao, Formacao } from '../formacao.model';

import { FormacaoService } from './formacao.service';

describe('Service Tests', () => {
  describe('Formacao Service', () => {
    let service: FormacaoService;
    let httpMock: HttpTestingController;
    let elemDefault: IFormacao;
    let expectedResult: IFormacao | IFormacao[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FormacaoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        instituicao: 'AAAAAAA',
        tipo: 'AAAAAAA',
        inicio: currentDate,
        conclusao: currentDate,
        criado: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            inicio: currentDate.format(DATE_FORMAT),
            conclusao: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Formacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            inicio: currentDate.format(DATE_FORMAT),
            conclusao: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inicio: currentDate,
            conclusao: currentDate,
            criado: currentDate,
          },
          returnedFromService
        );

        service.create(new Formacao()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Formacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            instituicao: 'BBBBBB',
            tipo: 'BBBBBB',
            inicio: currentDate.format(DATE_FORMAT),
            conclusao: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inicio: currentDate,
            conclusao: currentDate,
            criado: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Formacao', () => {
        const patchObject = Object.assign(
          {
            instituicao: 'BBBBBB',
            tipo: 'BBBBBB',
            inicio: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          new Formacao()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            inicio: currentDate,
            conclusao: currentDate,
            criado: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Formacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            instituicao: 'BBBBBB',
            tipo: 'BBBBBB',
            inicio: currentDate.format(DATE_FORMAT),
            conclusao: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inicio: currentDate,
            conclusao: currentDate,
            criado: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Formacao', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFormacaoToCollectionIfMissing', () => {
        it('should add a Formacao to an empty array', () => {
          const formacao: IFormacao = { id: 123 };
          expectedResult = service.addFormacaoToCollectionIfMissing([], formacao);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(formacao);
        });

        it('should not add a Formacao to an array that contains it', () => {
          const formacao: IFormacao = { id: 123 };
          const formacaoCollection: IFormacao[] = [
            {
              ...formacao,
            },
            { id: 456 },
          ];
          expectedResult = service.addFormacaoToCollectionIfMissing(formacaoCollection, formacao);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Formacao to an array that doesn't contain it", () => {
          const formacao: IFormacao = { id: 123 };
          const formacaoCollection: IFormacao[] = [{ id: 456 }];
          expectedResult = service.addFormacaoToCollectionIfMissing(formacaoCollection, formacao);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(formacao);
        });

        it('should add only unique Formacao to an array', () => {
          const formacaoArray: IFormacao[] = [{ id: 123 }, { id: 456 }, { id: 33816 }];
          const formacaoCollection: IFormacao[] = [{ id: 123 }];
          expectedResult = service.addFormacaoToCollectionIfMissing(formacaoCollection, ...formacaoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const formacao: IFormacao = { id: 123 };
          const formacao2: IFormacao = { id: 456 };
          expectedResult = service.addFormacaoToCollectionIfMissing([], formacao, formacao2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(formacao);
          expect(expectedResult).toContain(formacao2);
        });

        it('should accept null and undefined values', () => {
          const formacao: IFormacao = { id: 123 };
          expectedResult = service.addFormacaoToCollectionIfMissing([], null, formacao, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(formacao);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
