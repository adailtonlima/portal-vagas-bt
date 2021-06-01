import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { NivelIdioma } from 'app/entities/enumerations/nivel-idioma.model';
import { IIdioma, Idioma } from '../idioma.model';

import { IdiomaService } from './idioma.service';

describe('Service Tests', () => {
  describe('Idioma Service', () => {
    let service: IdiomaService;
    let httpMock: HttpTestingController;
    let elemDefault: IIdioma;
    let expectedResult: IIdioma | IIdioma[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IdiomaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        nivel: NivelIdioma.LEITURA,
        criado: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Idioma', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.create(new Idioma()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Idioma', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            nivel: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Idioma', () => {
        const patchObject = Object.assign(
          {
            nome: 'BBBBBB',
          },
          new Idioma()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Idioma', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            nivel: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should delete a Idioma', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIdiomaToCollectionIfMissing', () => {
        it('should add a Idioma to an empty array', () => {
          const idioma: IIdioma = { id: 123 };
          expectedResult = service.addIdiomaToCollectionIfMissing([], idioma);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(idioma);
        });

        it('should not add a Idioma to an array that contains it', () => {
          const idioma: IIdioma = { id: 123 };
          const idiomaCollection: IIdioma[] = [
            {
              ...idioma,
            },
            { id: 456 },
          ];
          expectedResult = service.addIdiomaToCollectionIfMissing(idiomaCollection, idioma);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Idioma to an array that doesn't contain it", () => {
          const idioma: IIdioma = { id: 123 };
          const idiomaCollection: IIdioma[] = [{ id: 456 }];
          expectedResult = service.addIdiomaToCollectionIfMissing(idiomaCollection, idioma);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(idioma);
        });

        it('should add only unique Idioma to an array', () => {
          const idiomaArray: IIdioma[] = [{ id: 123 }, { id: 456 }, { id: 68915 }];
          const idiomaCollection: IIdioma[] = [{ id: 123 }];
          expectedResult = service.addIdiomaToCollectionIfMissing(idiomaCollection, ...idiomaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const idioma: IIdioma = { id: 123 };
          const idioma2: IIdioma = { id: 456 };
          expectedResult = service.addIdiomaToCollectionIfMissing([], idioma, idioma2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(idioma);
          expect(expectedResult).toContain(idioma2);
        });

        it('should accept null and undefined values', () => {
          const idioma: IIdioma = { id: 123 };
          expectedResult = service.addIdiomaToCollectionIfMissing([], null, idioma, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(idioma);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
