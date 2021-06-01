import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExperienciaProfissional, ExperienciaProfissional } from '../experiencia-profissional.model';

import { ExperienciaProfissionalService } from './experiencia-profissional.service';

describe('Service Tests', () => {
  describe('ExperienciaProfissional Service', () => {
    let service: ExperienciaProfissionalService;
    let httpMock: HttpTestingController;
    let elemDefault: IExperienciaProfissional;
    let expectedResult: IExperienciaProfissional | IExperienciaProfissional[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExperienciaProfissionalService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        empresa: 'AAAAAAA',
        cargo: 'AAAAAAA',
        segmento: 'AAAAAAA',
        porte: 'AAAAAAA',
        inicio: currentDate,
        fim: currentDate,
        descricaoAtividade: 'AAAAAAA',
        salario: 0,
        beneficios: 0,
        criado: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            inicio: currentDate.format(DATE_FORMAT),
            fim: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ExperienciaProfissional', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            inicio: currentDate.format(DATE_FORMAT),
            fim: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inicio: currentDate,
            fim: currentDate,
            criado: currentDate,
          },
          returnedFromService
        );

        service.create(new ExperienciaProfissional()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExperienciaProfissional', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            empresa: 'BBBBBB',
            cargo: 'BBBBBB',
            segmento: 'BBBBBB',
            porte: 'BBBBBB',
            inicio: currentDate.format(DATE_FORMAT),
            fim: currentDate.format(DATE_FORMAT),
            descricaoAtividade: 'BBBBBB',
            salario: 1,
            beneficios: 1,
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inicio: currentDate,
            fim: currentDate,
            criado: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ExperienciaProfissional', () => {
        const patchObject = Object.assign(
          {
            inicio: currentDate.format(DATE_FORMAT),
            fim: currentDate.format(DATE_FORMAT),
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          new ExperienciaProfissional()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            inicio: currentDate,
            fim: currentDate,
            criado: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ExperienciaProfissional', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            empresa: 'BBBBBB',
            cargo: 'BBBBBB',
            segmento: 'BBBBBB',
            porte: 'BBBBBB',
            inicio: currentDate.format(DATE_FORMAT),
            fim: currentDate.format(DATE_FORMAT),
            descricaoAtividade: 'BBBBBB',
            salario: 1,
            beneficios: 1,
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inicio: currentDate,
            fim: currentDate,
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

      it('should delete a ExperienciaProfissional', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExperienciaProfissionalToCollectionIfMissing', () => {
        it('should add a ExperienciaProfissional to an empty array', () => {
          const experienciaProfissional: IExperienciaProfissional = { id: 123 };
          expectedResult = service.addExperienciaProfissionalToCollectionIfMissing([], experienciaProfissional);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(experienciaProfissional);
        });

        it('should not add a ExperienciaProfissional to an array that contains it', () => {
          const experienciaProfissional: IExperienciaProfissional = { id: 123 };
          const experienciaProfissionalCollection: IExperienciaProfissional[] = [
            {
              ...experienciaProfissional,
            },
            { id: 456 },
          ];
          expectedResult = service.addExperienciaProfissionalToCollectionIfMissing(
            experienciaProfissionalCollection,
            experienciaProfissional
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ExperienciaProfissional to an array that doesn't contain it", () => {
          const experienciaProfissional: IExperienciaProfissional = { id: 123 };
          const experienciaProfissionalCollection: IExperienciaProfissional[] = [{ id: 456 }];
          expectedResult = service.addExperienciaProfissionalToCollectionIfMissing(
            experienciaProfissionalCollection,
            experienciaProfissional
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(experienciaProfissional);
        });

        it('should add only unique ExperienciaProfissional to an array', () => {
          const experienciaProfissionalArray: IExperienciaProfissional[] = [{ id: 123 }, { id: 456 }, { id: 98170 }];
          const experienciaProfissionalCollection: IExperienciaProfissional[] = [{ id: 123 }];
          expectedResult = service.addExperienciaProfissionalToCollectionIfMissing(
            experienciaProfissionalCollection,
            ...experienciaProfissionalArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const experienciaProfissional: IExperienciaProfissional = { id: 123 };
          const experienciaProfissional2: IExperienciaProfissional = { id: 456 };
          expectedResult = service.addExperienciaProfissionalToCollectionIfMissing([], experienciaProfissional, experienciaProfissional2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(experienciaProfissional);
          expect(expectedResult).toContain(experienciaProfissional2);
        });

        it('should accept null and undefined values', () => {
          const experienciaProfissional: IExperienciaProfissional = { id: 123 };
          expectedResult = service.addExperienciaProfissionalToCollectionIfMissing([], null, experienciaProfissional, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(experienciaProfissional);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
