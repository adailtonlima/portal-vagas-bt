jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EnderecoService } from '../service/endereco.service';
import { IEndereco, Endereco } from '../endereco.model';

import { EnderecoUpdateComponent } from './endereco-update.component';

describe('Component Tests', () => {
  describe('Endereco Management Update Component', () => {
    let comp: EnderecoUpdateComponent;
    let fixture: ComponentFixture<EnderecoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let enderecoService: EnderecoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EnderecoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EnderecoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnderecoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      enderecoService = TestBed.inject(EnderecoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const endereco: IEndereco = { id: 456 };

        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(endereco));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const endereco = { id: 123 };
        spyOn(enderecoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: endereco }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(enderecoService.update).toHaveBeenCalledWith(endereco);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const endereco = new Endereco();
        spyOn(enderecoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: endereco }));
        saveSubject.complete();

        // THEN
        expect(enderecoService.create).toHaveBeenCalledWith(endereco);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const endereco = { id: 123 };
        spyOn(enderecoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ endereco });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(enderecoService.update).toHaveBeenCalledWith(endereco);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
