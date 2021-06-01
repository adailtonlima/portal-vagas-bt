jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AreaAtuacaoService } from '../service/area-atuacao.service';
import { IAreaAtuacao, AreaAtuacao } from '../area-atuacao.model';

import { AreaAtuacaoUpdateComponent } from './area-atuacao-update.component';

describe('Component Tests', () => {
  describe('AreaAtuacao Management Update Component', () => {
    let comp: AreaAtuacaoUpdateComponent;
    let fixture: ComponentFixture<AreaAtuacaoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let areaAtuacaoService: AreaAtuacaoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AreaAtuacaoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AreaAtuacaoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AreaAtuacaoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      areaAtuacaoService = TestBed.inject(AreaAtuacaoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const areaAtuacao: IAreaAtuacao = { id: 456 };

        activatedRoute.data = of({ areaAtuacao });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(areaAtuacao));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const areaAtuacao = { id: 123 };
        spyOn(areaAtuacaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ areaAtuacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: areaAtuacao }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(areaAtuacaoService.update).toHaveBeenCalledWith(areaAtuacao);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const areaAtuacao = new AreaAtuacao();
        spyOn(areaAtuacaoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ areaAtuacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: areaAtuacao }));
        saveSubject.complete();

        // THEN
        expect(areaAtuacaoService.create).toHaveBeenCalledWith(areaAtuacao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const areaAtuacao = { id: 123 };
        spyOn(areaAtuacaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ areaAtuacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(areaAtuacaoService.update).toHaveBeenCalledWith(areaAtuacao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
