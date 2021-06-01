jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FuncaoPessoaService } from '../service/funcao-pessoa.service';
import { IFuncaoPessoa, FuncaoPessoa } from '../funcao-pessoa.model';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';

import { FuncaoPessoaUpdateComponent } from './funcao-pessoa-update.component';

describe('Component Tests', () => {
  describe('FuncaoPessoa Management Update Component', () => {
    let comp: FuncaoPessoaUpdateComponent;
    let fixture: ComponentFixture<FuncaoPessoaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let funcaoPessoaService: FuncaoPessoaService;
    let pessoaService: PessoaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FuncaoPessoaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FuncaoPessoaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FuncaoPessoaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      funcaoPessoaService = TestBed.inject(FuncaoPessoaService);
      pessoaService = TestBed.inject(PessoaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Pessoa query and add missing value', () => {
        const funcaoPessoa: IFuncaoPessoa = { id: 456 };
        const pessoa: IPessoa = { id: 58406 };
        funcaoPessoa.pessoa = pessoa;

        const pessoaCollection: IPessoa[] = [{ id: 26304 }];
        spyOn(pessoaService, 'query').and.returnValue(of(new HttpResponse({ body: pessoaCollection })));
        const additionalPessoas = [pessoa];
        const expectedCollection: IPessoa[] = [...additionalPessoas, ...pessoaCollection];
        spyOn(pessoaService, 'addPessoaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ funcaoPessoa });
        comp.ngOnInit();

        expect(pessoaService.query).toHaveBeenCalled();
        expect(pessoaService.addPessoaToCollectionIfMissing).toHaveBeenCalledWith(pessoaCollection, ...additionalPessoas);
        expect(comp.pessoasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const funcaoPessoa: IFuncaoPessoa = { id: 456 };
        const pessoa: IPessoa = { id: 36780 };
        funcaoPessoa.pessoa = pessoa;

        activatedRoute.data = of({ funcaoPessoa });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(funcaoPessoa));
        expect(comp.pessoasSharedCollection).toContain(pessoa);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const funcaoPessoa = { id: 123 };
        spyOn(funcaoPessoaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ funcaoPessoa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: funcaoPessoa }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(funcaoPessoaService.update).toHaveBeenCalledWith(funcaoPessoa);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const funcaoPessoa = new FuncaoPessoa();
        spyOn(funcaoPessoaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ funcaoPessoa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: funcaoPessoa }));
        saveSubject.complete();

        // THEN
        expect(funcaoPessoaService.create).toHaveBeenCalledWith(funcaoPessoa);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const funcaoPessoa = { id: 123 };
        spyOn(funcaoPessoaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ funcaoPessoa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(funcaoPessoaService.update).toHaveBeenCalledWith(funcaoPessoa);
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
