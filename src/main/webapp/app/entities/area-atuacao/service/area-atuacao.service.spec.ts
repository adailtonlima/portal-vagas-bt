import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAreaAtuacao, AreaAtuacao } from '../area-atuacao.model';

import { AreaAtuacaoService } from './area-atuacao.service';

describe('Service Tests', () => {
  describe('AreaAtuacao Service', () => {
    let service: AreaAtuacaoService;
    let httpMock: HttpTestingController;
    let elemDefault: IAreaAtuacao;
    let expectedResult: IAreaAtuacao | IAreaAtuacao[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AreaAtuacaoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        descricao: 'AAAAAAA',
        iconeUrl: 'AAAAAAA',
        ativo: false,
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

      it('should create a AreaAtuacao', () => {
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

        service.create(new AreaAtuacao()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AreaAtuacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            descricao: 'BBBBBB',
            iconeUrl: 'BBBBBB',
            ativo: true,
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

      it('should partial update a AreaAtuacao', () => {
        const patchObject = Object.assign(
          {
            descricao: 'BBBBBB',
            ativo: true,
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          new AreaAtuacao()
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

      it('should return a list of AreaAtuacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            descricao: 'BBBBBB',
            iconeUrl: 'BBBBBB',
            ativo: true,
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

      it('should delete a AreaAtuacao', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAreaAtuacaoToCollectionIfMissing', () => {
        it('should add a AreaAtuacao to an empty array', () => {
          const areaAtuacao: IAreaAtuacao = { id: 123 };
          expectedResult = service.addAreaAtuacaoToCollectionIfMissing([], areaAtuacao);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(areaAtuacao);
        });

        it('should not add a AreaAtuacao to an array that contains it', () => {
          const areaAtuacao: IAreaAtuacao = { id: 123 };
          const areaAtuacaoCollection: IAreaAtuacao[] = [
            {
              ...areaAtuacao,
            },
            { id: 456 },
          ];
          expectedResult = service.addAreaAtuacaoToCollectionIfMissing(areaAtuacaoCollection, areaAtuacao);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AreaAtuacao to an array that doesn't contain it", () => {
          const areaAtuacao: IAreaAtuacao = { id: 123 };
          const areaAtuacaoCollection: IAreaAtuacao[] = [{ id: 456 }];
          expectedResult = service.addAreaAtuacaoToCollectionIfMissing(areaAtuacaoCollection, areaAtuacao);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(areaAtuacao);
        });

        it('should add only unique AreaAtuacao to an array', () => {
          const areaAtuacaoArray: IAreaAtuacao[] = [{ id: 123 }, { id: 456 }, { id: 60086 }];
          const areaAtuacaoCollection: IAreaAtuacao[] = [{ id: 123 }];
          expectedResult = service.addAreaAtuacaoToCollectionIfMissing(areaAtuacaoCollection, ...areaAtuacaoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const areaAtuacao: IAreaAtuacao = { id: 123 };
          const areaAtuacao2: IAreaAtuacao = { id: 456 };
          expectedResult = service.addAreaAtuacaoToCollectionIfMissing([], areaAtuacao, areaAtuacao2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(areaAtuacao);
          expect(expectedResult).toContain(areaAtuacao2);
        });

        it('should accept null and undefined values', () => {
          const areaAtuacao: IAreaAtuacao = { id: 123 };
          expectedResult = service.addAreaAtuacaoToCollectionIfMissing([], null, areaAtuacao, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(areaAtuacao);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
