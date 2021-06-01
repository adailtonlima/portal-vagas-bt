jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVaga, Vaga } from '../vaga.model';
import { VagaService } from '../service/vaga.service';

import { VagaRoutingResolveService } from './vaga-routing-resolve.service';

describe('Service Tests', () => {
  describe('Vaga routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VagaRoutingResolveService;
    let service: VagaService;
    let resultVaga: IVaga | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VagaRoutingResolveService);
      service = TestBed.inject(VagaService);
      resultVaga = undefined;
    });

    describe('resolve', () => {
      it('should return IVaga returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVaga = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVaga).toEqual({ id: 123 });
      });

      it('should return new IVaga if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVaga = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVaga).toEqual(new Vaga());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVaga = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVaga).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
