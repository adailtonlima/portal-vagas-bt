import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPerfilProfissional, PerfilProfissional } from '../perfil-profissional.model';

import { PerfilProfissionalService } from './perfil-profissional.service';

describe('Service Tests', () => {
  describe('PerfilProfissional Service', () => {
    let service: PerfilProfissionalService;
    let httpMock: HttpTestingController;
    let elemDefault: IPerfilProfissional;
    let expectedResult: IPerfilProfissional | IPerfilProfissional[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PerfilProfissionalService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        estagio: false,
        procurandoEmprego: false,
        objetivosPessoais: 'AAAAAAA',
        pretensaoSalarial: 0,
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

      it('should create a PerfilProfissional', () => {
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

        service.create(new PerfilProfissional()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PerfilProfissional', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            estagio: true,
            procurandoEmprego: true,
            objetivosPessoais: 'BBBBBB',
            pretensaoSalarial: 1,
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

      it('should partial update a PerfilProfissional', () => {
        const patchObject = Object.assign(
          {
            objetivosPessoais: 'BBBBBB',
          },
          new PerfilProfissional()
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

      it('should return a list of PerfilProfissional', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            estagio: true,
            procurandoEmprego: true,
            objetivosPessoais: 'BBBBBB',
            pretensaoSalarial: 1,
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

      it('should delete a PerfilProfissional', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPerfilProfissionalToCollectionIfMissing', () => {
        it('should add a PerfilProfissional to an empty array', () => {
          const perfilProfissional: IPerfilProfissional = { id: 123 };
          expectedResult = service.addPerfilProfissionalToCollectionIfMissing([], perfilProfissional);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(perfilProfissional);
        });

        it('should not add a PerfilProfissional to an array that contains it', () => {
          const perfilProfissional: IPerfilProfissional = { id: 123 };
          const perfilProfissionalCollection: IPerfilProfissional[] = [
            {
              ...perfilProfissional,
            },
            { id: 456 },
          ];
          expectedResult = service.addPerfilProfissionalToCollectionIfMissing(perfilProfissionalCollection, perfilProfissional);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PerfilProfissional to an array that doesn't contain it", () => {
          const perfilProfissional: IPerfilProfissional = { id: 123 };
          const perfilProfissionalCollection: IPerfilProfissional[] = [{ id: 456 }];
          expectedResult = service.addPerfilProfissionalToCollectionIfMissing(perfilProfissionalCollection, perfilProfissional);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(perfilProfissional);
        });

        it('should add only unique PerfilProfissional to an array', () => {
          const perfilProfissionalArray: IPerfilProfissional[] = [{ id: 123 }, { id: 456 }, { id: 78828 }];
          const perfilProfissionalCollection: IPerfilProfissional[] = [{ id: 123 }];
          expectedResult = service.addPerfilProfissionalToCollectionIfMissing(perfilProfissionalCollection, ...perfilProfissionalArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const perfilProfissional: IPerfilProfissional = { id: 123 };
          const perfilProfissional2: IPerfilProfissional = { id: 456 };
          expectedResult = service.addPerfilProfissionalToCollectionIfMissing([], perfilProfissional, perfilProfissional2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(perfilProfissional);
          expect(expectedResult).toContain(perfilProfissional2);
        });

        it('should accept null and undefined values', () => {
          const perfilProfissional: IPerfilProfissional = { id: 123 };
          expectedResult = service.addPerfilProfissionalToCollectionIfMissing([], null, perfilProfissional, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(perfilProfissional);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
