jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExperienciaProfissional, ExperienciaProfissional } from '../experiencia-profissional.model';
import { ExperienciaProfissionalService } from '../service/experiencia-profissional.service';

import { ExperienciaProfissionalRoutingResolveService } from './experiencia-profissional-routing-resolve.service';

describe('Service Tests', () => {
  describe('ExperienciaProfissional routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ExperienciaProfissionalRoutingResolveService;
    let service: ExperienciaProfissionalService;
    let resultExperienciaProfissional: IExperienciaProfissional | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ExperienciaProfissionalRoutingResolveService);
      service = TestBed.inject(ExperienciaProfissionalService);
      resultExperienciaProfissional = undefined;
    });

    describe('resolve', () => {
      it('should return IExperienciaProfissional returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExperienciaProfissional = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExperienciaProfissional).toEqual({ id: 123 });
      });

      it('should return new IExperienciaProfissional if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExperienciaProfissional = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultExperienciaProfissional).toEqual(new ExperienciaProfissional());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExperienciaProfissional = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExperienciaProfissional).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
