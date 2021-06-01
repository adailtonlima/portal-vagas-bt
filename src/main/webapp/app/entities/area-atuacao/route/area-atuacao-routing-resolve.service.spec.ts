jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAreaAtuacao, AreaAtuacao } from '../area-atuacao.model';
import { AreaAtuacaoService } from '../service/area-atuacao.service';

import { AreaAtuacaoRoutingResolveService } from './area-atuacao-routing-resolve.service';

describe('Service Tests', () => {
  describe('AreaAtuacao routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AreaAtuacaoRoutingResolveService;
    let service: AreaAtuacaoService;
    let resultAreaAtuacao: IAreaAtuacao | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AreaAtuacaoRoutingResolveService);
      service = TestBed.inject(AreaAtuacaoService);
      resultAreaAtuacao = undefined;
    });

    describe('resolve', () => {
      it('should return IAreaAtuacao returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAreaAtuacao = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAreaAtuacao).toEqual({ id: 123 });
      });

      it('should return new IAreaAtuacao if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAreaAtuacao = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAreaAtuacao).toEqual(new AreaAtuacao());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAreaAtuacao = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAreaAtuacao).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
