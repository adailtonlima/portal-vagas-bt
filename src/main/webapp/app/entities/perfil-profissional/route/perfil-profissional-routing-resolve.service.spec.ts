jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPerfilProfissional, PerfilProfissional } from '../perfil-profissional.model';
import { PerfilProfissionalService } from '../service/perfil-profissional.service';

import { PerfilProfissionalRoutingResolveService } from './perfil-profissional-routing-resolve.service';

describe('Service Tests', () => {
  describe('PerfilProfissional routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PerfilProfissionalRoutingResolveService;
    let service: PerfilProfissionalService;
    let resultPerfilProfissional: IPerfilProfissional | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PerfilProfissionalRoutingResolveService);
      service = TestBed.inject(PerfilProfissionalService);
      resultPerfilProfissional = undefined;
    });

    describe('resolve', () => {
      it('should return IPerfilProfissional returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPerfilProfissional = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPerfilProfissional).toEqual({ id: 123 });
      });

      it('should return new IPerfilProfissional if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPerfilProfissional = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPerfilProfissional).toEqual(new PerfilProfissional());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPerfilProfissional = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPerfilProfissional).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
