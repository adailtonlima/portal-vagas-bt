import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVaga, Vaga } from '../vaga.model';

import { VagaService } from './vaga.service';

describe('Service Tests', () => {
  describe('Vaga Service', () => {
    let service: VagaService;
    let httpMock: HttpTestingController;
    let elemDefault: IVaga;
    let expectedResult: IVaga | IVaga[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VagaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        descricao: 'AAAAAAA',
        titulo: 'AAAAAAA',
        estagio: false,
        salario: 0,
        beneficios: 0,
        jornadaSemanal: 0,
        bannerUrl: 'AAAAAAA',
        fonte: 'AAAAAAA',
        linkRecrutamento: 'AAAAAAA',
        ativo: false,
        preenchida: false,
        criado: currentDate,
        prazoAnuncio: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            criado: currentDate.format(DATE_TIME_FORMAT),
            prazoAnuncio: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Vaga', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            criado: currentDate.format(DATE_TIME_FORMAT),
            prazoAnuncio: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
            prazoAnuncio: currentDate,
          },
          returnedFromService
        );

        service.create(new Vaga()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vaga', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            descricao: 'BBBBBB',
            titulo: 'BBBBBB',
            estagio: true,
            salario: 1,
            beneficios: 1,
            jornadaSemanal: 1,
            bannerUrl: 'BBBBBB',
            fonte: 'BBBBBB',
            linkRecrutamento: 'BBBBBB',
            ativo: true,
            preenchida: true,
            criado: currentDate.format(DATE_TIME_FORMAT),
            prazoAnuncio: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
            prazoAnuncio: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Vaga', () => {
        const patchObject = Object.assign(
          {
            titulo: 'BBBBBB',
            jornadaSemanal: 1,
            fonte: 'BBBBBB',
            ativo: true,
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          new Vaga()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            criado: currentDate,
            prazoAnuncio: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Vaga', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            descricao: 'BBBBBB',
            titulo: 'BBBBBB',
            estagio: true,
            salario: 1,
            beneficios: 1,
            jornadaSemanal: 1,
            bannerUrl: 'BBBBBB',
            fonte: 'BBBBBB',
            linkRecrutamento: 'BBBBBB',
            ativo: true,
            preenchida: true,
            criado: currentDate.format(DATE_TIME_FORMAT),
            prazoAnuncio: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
            prazoAnuncio: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Vaga', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVagaToCollectionIfMissing', () => {
        it('should add a Vaga to an empty array', () => {
          const vaga: IVaga = { id: 123 };
          expectedResult = service.addVagaToCollectionIfMissing([], vaga);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vaga);
        });

        it('should not add a Vaga to an array that contains it', () => {
          const vaga: IVaga = { id: 123 };
          const vagaCollection: IVaga[] = [
            {
              ...vaga,
            },
            { id: 456 },
          ];
          expectedResult = service.addVagaToCollectionIfMissing(vagaCollection, vaga);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Vaga to an array that doesn't contain it", () => {
          const vaga: IVaga = { id: 123 };
          const vagaCollection: IVaga[] = [{ id: 456 }];
          expectedResult = service.addVagaToCollectionIfMissing(vagaCollection, vaga);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vaga);
        });

        it('should add only unique Vaga to an array', () => {
          const vagaArray: IVaga[] = [{ id: 123 }, { id: 456 }, { id: 9306 }];
          const vagaCollection: IVaga[] = [{ id: 123 }];
          expectedResult = service.addVagaToCollectionIfMissing(vagaCollection, ...vagaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const vaga: IVaga = { id: 123 };
          const vaga2: IVaga = { id: 456 };
          expectedResult = service.addVagaToCollectionIfMissing([], vaga, vaga2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vaga);
          expect(expectedResult).toContain(vaga2);
        });

        it('should accept null and undefined values', () => {
          const vaga: IVaga = { id: 123 };
          expectedResult = service.addVagaToCollectionIfMissing([], null, vaga, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vaga);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
