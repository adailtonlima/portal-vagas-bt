jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIdioma, Idioma } from '../idioma.model';
import { IdiomaService } from '../service/idioma.service';

import { IdiomaRoutingResolveService } from './idioma-routing-resolve.service';

describe('Service Tests', () => {
  describe('Idioma routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IdiomaRoutingResolveService;
    let service: IdiomaService;
    let resultIdioma: IIdioma | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IdiomaRoutingResolveService);
      service = TestBed.inject(IdiomaService);
      resultIdioma = undefined;
    });

    describe('resolve', () => {
      it('should return IIdioma returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIdioma = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIdioma).toEqual({ id: 123 });
      });

      it('should return new IIdioma if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIdioma = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIdioma).toEqual(new Idioma());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIdioma = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIdioma).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
