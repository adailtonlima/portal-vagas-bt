jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFuncaoPessoa, FuncaoPessoa } from '../funcao-pessoa.model';
import { FuncaoPessoaService } from '../service/funcao-pessoa.service';

import { FuncaoPessoaRoutingResolveService } from './funcao-pessoa-routing-resolve.service';

describe('Service Tests', () => {
  describe('FuncaoPessoa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FuncaoPessoaRoutingResolveService;
    let service: FuncaoPessoaService;
    let resultFuncaoPessoa: IFuncaoPessoa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FuncaoPessoaRoutingResolveService);
      service = TestBed.inject(FuncaoPessoaService);
      resultFuncaoPessoa = undefined;
    });

    describe('resolve', () => {
      it('should return IFuncaoPessoa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFuncaoPessoa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFuncaoPessoa).toEqual({ id: 123 });
      });

      it('should return new IFuncaoPessoa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFuncaoPessoa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFuncaoPessoa).toEqual(new FuncaoPessoa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFuncaoPessoa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFuncaoPessoa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
