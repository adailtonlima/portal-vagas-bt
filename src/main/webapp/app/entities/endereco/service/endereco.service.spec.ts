import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEndereco, Endereco } from '../endereco.model';

import { EnderecoService } from './endereco.service';

describe('Service Tests', () => {
  describe('Endereco Service', () => {
    let service: EnderecoService;
    let httpMock: HttpTestingController;
    let elemDefault: IEndereco;
    let expectedResult: IEndereco | IEndereco[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EnderecoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        cep: 'AAAAAAA',
        logradouro: 'AAAAAAA',
        complemento: 'AAAAAAA',
        numero: 'AAAAAAA',
        uf: 'AAAAAAA',
        municipio: 'AAAAAAA',
        bairro: 'AAAAAAA',
        latitude: 0,
        longitude: 0,
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

      it('should create a Endereco', () => {
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

        service.create(new Endereco()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Endereco', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cep: 'BBBBBB',
            logradouro: 'BBBBBB',
            complemento: 'BBBBBB',
            numero: 'BBBBBB',
            uf: 'BBBBBB',
            municipio: 'BBBBBB',
            bairro: 'BBBBBB',
            latitude: 1,
            longitude: 1,
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

      it('should partial update a Endereco', () => {
        const patchObject = Object.assign(
          {
            cep: 'BBBBBB',
            complemento: 'BBBBBB',
            latitude: 1,
            longitude: 1,
          },
          new Endereco()
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

      it('should return a list of Endereco', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cep: 'BBBBBB',
            logradouro: 'BBBBBB',
            complemento: 'BBBBBB',
            numero: 'BBBBBB',
            uf: 'BBBBBB',
            municipio: 'BBBBBB',
            bairro: 'BBBBBB',
            latitude: 1,
            longitude: 1,
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

      it('should delete a Endereco', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEnderecoToCollectionIfMissing', () => {
        it('should add a Endereco to an empty array', () => {
          const endereco: IEndereco = { id: 123 };
          expectedResult = service.addEnderecoToCollectionIfMissing([], endereco);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(endereco);
        });

        it('should not add a Endereco to an array that contains it', () => {
          const endereco: IEndereco = { id: 123 };
          const enderecoCollection: IEndereco[] = [
            {
              ...endereco,
            },
            { id: 456 },
          ];
          expectedResult = service.addEnderecoToCollectionIfMissing(enderecoCollection, endereco);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Endereco to an array that doesn't contain it", () => {
          const endereco: IEndereco = { id: 123 };
          const enderecoCollection: IEndereco[] = [{ id: 456 }];
          expectedResult = service.addEnderecoToCollectionIfMissing(enderecoCollection, endereco);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(endereco);
        });

        it('should add only unique Endereco to an array', () => {
          const enderecoArray: IEndereco[] = [{ id: 123 }, { id: 456 }, { id: 61489 }];
          const enderecoCollection: IEndereco[] = [{ id: 123 }];
          expectedResult = service.addEnderecoToCollectionIfMissing(enderecoCollection, ...enderecoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const endereco: IEndereco = { id: 123 };
          const endereco2: IEndereco = { id: 456 };
          expectedResult = service.addEnderecoToCollectionIfMissing([], endereco, endereco2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(endereco);
          expect(expectedResult).toContain(endereco2);
        });

        it('should accept null and undefined values', () => {
          const endereco: IEndereco = { id: 123 };
          expectedResult = service.addEnderecoToCollectionIfMissing([], null, endereco, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(endereco);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
