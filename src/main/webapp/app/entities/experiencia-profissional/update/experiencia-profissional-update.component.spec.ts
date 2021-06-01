jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExperienciaProfissionalService } from '../service/experiencia-profissional.service';
import { IExperienciaProfissional, ExperienciaProfissional } from '../experiencia-profissional.model';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';

import { ExperienciaProfissionalUpdateComponent } from './experiencia-profissional-update.component';

describe('Component Tests', () => {
  describe('ExperienciaProfissional Management Update Component', () => {
    let comp: ExperienciaProfissionalUpdateComponent;
    let fixture: ComponentFixture<ExperienciaProfissionalUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let experienciaProfissionalService: ExperienciaProfissionalService;
    let pessoaService: PessoaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExperienciaProfissionalUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExperienciaProfissionalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExperienciaProfissionalUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      experienciaProfissionalService = TestBed.inject(ExperienciaProfissionalService);
      pessoaService = TestBed.inject(PessoaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Pessoa query and add missing value', () => {
        const experienciaProfissional: IExperienciaProfissional = { id: 456 };
        const pessoa: IPessoa = { id: 66870 };
        experienciaProfissional.pessoa = pessoa;

        const pessoaCollection: IPessoa[] = [{ id: 65668 }];
        spyOn(pessoaService, 'query').and.returnValue(of(new HttpResponse({ body: pessoaCollection })));
        const additionalPessoas = [pessoa];
        const expectedCollection: IPessoa[] = [...additionalPessoas, ...pessoaCollection];
        spyOn(pessoaService, 'addPessoaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ experienciaProfissional });
        comp.ngOnInit();

        expect(pessoaService.query).toHaveBeenCalled();
        expect(pessoaService.addPessoaToCollectionIfMissing).toHaveBeenCalledWith(pessoaCollection, ...additionalPessoas);
        expect(comp.pessoasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const experienciaProfissional: IExperienciaProfissional = { id: 456 };
        const pessoa: IPessoa = { id: 54158 };
        experienciaProfissional.pessoa = pessoa;

        activatedRoute.data = of({ experienciaProfissional });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(experienciaProfissional));
        expect(comp.pessoasSharedCollection).toContain(pessoa);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const experienciaProfissional = { id: 123 };
        spyOn(experienciaProfissionalService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ experienciaProfissional });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: experienciaProfissional }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(experienciaProfissionalService.update).toHaveBeenCalledWith(experienciaProfissional);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const experienciaProfissional = new ExperienciaProfissional();
        spyOn(experienciaProfissionalService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ experienciaProfissional });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: experienciaProfissional }));
        saveSubject.complete();

        // THEN
        expect(experienciaProfissionalService.create).toHaveBeenCalledWith(experienciaProfissional);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const experienciaProfissional = { id: 123 };
        spyOn(experienciaProfissionalService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ experienciaProfissional });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(experienciaProfissionalService.update).toHaveBeenCalledWith(experienciaProfissional);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPessoaById', () => {
        it('Should return tracked Pessoa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPessoaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
