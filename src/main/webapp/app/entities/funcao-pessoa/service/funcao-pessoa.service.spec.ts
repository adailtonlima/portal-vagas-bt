import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Funcao } from 'app/entities/enumerations/funcao.model';
import { IFuncaoPessoa, FuncaoPessoa } from '../funcao-pessoa.model';

import { FuncaoPessoaService } from './funcao-pessoa.service';

describe('Service Tests', () => {
  describe('FuncaoPessoa Service', () => {
    let service: FuncaoPessoaService;
    let httpMock: HttpTestingController;
    let elemDefault: IFuncaoPessoa;
    let expectedResult: IFuncaoPessoa | IFuncaoPessoa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FuncaoPessoaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        funcao: Funcao.ADMIN,
        criado: currentDate,
        ativo: false,
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

      it('should create a FuncaoPessoa', () => {
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

        service.create(new FuncaoPessoa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FuncaoPessoa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            funcao: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
            ativo: true,
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

      it('should partial update a FuncaoPessoa', () => {
        const patchObject = Object.assign(
          {
            funcao: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          new FuncaoPessoa()
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

      it('should return a list of FuncaoPessoa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            funcao: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
            ativo: true,
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

      it('should delete a FuncaoPessoa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFuncaoPessoaToCollectionIfMissing', () => {
        it('should add a FuncaoPessoa to an empty array', () => {
          const funcaoPessoa: IFuncaoPessoa = { id: 123 };
          expectedResult = service.addFuncaoPessoaToCollectionIfMissing([], funcaoPessoa);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(funcaoPessoa);
        });

        it('should not add a FuncaoPessoa to an array that contains it', () => {
          const funcaoPessoa: IFuncaoPessoa = { id: 123 };
          const funcaoPessoaCollection: IFuncaoPessoa[] = [
            {
              ...funcaoPessoa,
            },
            { id: 456 },
          ];
          expectedResult = service.addFuncaoPessoaToCollectionIfMissing(funcaoPessoaCollection, funcaoPessoa);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FuncaoPessoa to an array that doesn't contain it", () => {
          const funcaoPessoa: IFuncaoPessoa = { id: 123 };
          const funcaoPessoaCollection: IFuncaoPessoa[] = [{ id: 456 }];
          expectedResult = service.addFuncaoPessoaToCollectionIfMissing(funcaoPessoaCollection, funcaoPessoa);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(funcaoPessoa);
        });

        it('should add only unique FuncaoPessoa to an array', () => {
          const funcaoPessoaArray: IFuncaoPessoa[] = [{ id: 123 }, { id: 456 }, { id: 19810 }];
          const funcaoPessoaCollection: IFuncaoPessoa[] = [{ id: 123 }];
          expectedResult = service.addFuncaoPessoaToCollectionIfMissing(funcaoPessoaCollection, ...funcaoPessoaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const funcaoPessoa: IFuncaoPessoa = { id: 123 };
          const funcaoPessoa2: IFuncaoPessoa = { id: 456 };
          expectedResult = service.addFuncaoPessoaToCollectionIfMissing([], funcaoPessoa, funcaoPessoa2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(funcaoPessoa);
          expect(expectedResult).toContain(funcaoPessoa2);
        });

        it('should accept null and undefined values', () => {
          const funcaoPessoa: IFuncaoPessoa = { id: 123 };
          expectedResult = service.addFuncaoPessoaToCollectionIfMissing([], null, funcaoPessoa, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(funcaoPessoa);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
