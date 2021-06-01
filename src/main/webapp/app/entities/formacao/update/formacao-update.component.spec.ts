jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FormacaoService } from '../service/formacao.service';
import { IFormacao, Formacao } from '../formacao.model';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';

import { FormacaoUpdateComponent } from './formacao-update.component';

describe('Component Tests', () => {
  describe('Formacao Management Update Component', () => {
    let comp: FormacaoUpdateComponent;
    let fixture: ComponentFixture<FormacaoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let formacaoService: FormacaoService;
    let pessoaService: PessoaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FormacaoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FormacaoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormacaoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      formacaoService = TestBed.inject(FormacaoService);
      pessoaService = TestBed.inject(PessoaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Pessoa query and add missing value', () => {
        const formacao: IFormacao = { id: 456 };
        const pessoa: IPessoa = { id: 75878 };
        formacao.pessoa = pessoa;

        const pessoaCollection: IPessoa[] = [{ id: 89087 }];
        spyOn(pessoaService, 'query').and.returnValue(of(new HttpResponse({ body: pessoaCollection })));
        const additionalPessoas = [pessoa];
        const expectedCollection: IPessoa[] = [...additionalPessoas, ...pessoaCollection];
        spyOn(pessoaService, 'addPessoaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ formacao });
        comp.ngOnInit();

        expect(pessoaService.query).toHaveBeenCalled();
        expect(pessoaService.addPessoaToCollectionIfMissing).toHaveBeenCalledWith(pessoaCollection, ...additionalPessoas);
        expect(comp.pessoasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const formacao: IFormacao = { id: 456 };
        const pessoa: IPessoa = { id: 96326 };
        formacao.pessoa = pessoa;

        activatedRoute.data = of({ formacao });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(formacao));
        expect(comp.pessoasSharedCollection).toContain(pessoa);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const formacao = { id: 123 };
        spyOn(formacaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ formacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: formacao }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(formacaoService.update).toHaveBeenCalledWith(formacao);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const formacao = new Formacao();
        spyOn(formacaoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ formacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: formacao }));
        saveSubject.complete();

        // THEN
        expect(formacaoService.create).toHaveBeenCalledWith(formacao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const formacao = { id: 123 };
        spyOn(formacaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ formacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(formacaoService.update).toHaveBeenCalledWith(formacao);
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
