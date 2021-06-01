jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFormacao, Formacao } from '../formacao.model';
import { FormacaoService } from '../service/formacao.service';

import { FormacaoRoutingResolveService } from './formacao-routing-resolve.service';

describe('Service Tests', () => {
  describe('Formacao routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FormacaoRoutingResolveService;
    let service: FormacaoService;
    let resultFormacao: IFormacao | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FormacaoRoutingResolveService);
      service = TestBed.inject(FormacaoService);
      resultFormacao = undefined;
    });

    describe('resolve', () => {
      it('should return IFormacao returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFormacao = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFormacao).toEqual({ id: 123 });
      });

      it('should return new IFormacao if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFormacao = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFormacao).toEqual(new Formacao());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFormacao = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFormacao).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
