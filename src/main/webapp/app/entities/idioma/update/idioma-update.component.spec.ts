jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IdiomaService } from '../service/idioma.service';
import { IIdioma, Idioma } from '../idioma.model';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';

import { IdiomaUpdateComponent } from './idioma-update.component';

describe('Component Tests', () => {
  describe('Idioma Management Update Component', () => {
    let comp: IdiomaUpdateComponent;
    let fixture: ComponentFixture<IdiomaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let idiomaService: IdiomaService;
    let pessoaService: PessoaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IdiomaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IdiomaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IdiomaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      idiomaService = TestBed.inject(IdiomaService);
      pessoaService = TestBed.inject(PessoaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Pessoa query and add missing value', () => {
        const idioma: IIdioma = { id: 456 };
        const pessoa: IPessoa = { id: 90475 };
        idioma.pessoa = pessoa;

        const pessoaCollection: IPessoa[] = [{ id: 87782 }];
        spyOn(pessoaService, 'query').and.returnValue(of(new HttpResponse({ body: pessoaCollection })));
        const additionalPessoas = [pessoa];
        const expectedCollection: IPessoa[] = [...additionalPessoas, ...pessoaCollection];
        spyOn(pessoaService, 'addPessoaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ idioma });
        comp.ngOnInit();

        expect(pessoaService.query).toHaveBeenCalled();
        expect(pessoaService.addPessoaToCollectionIfMissing).toHaveBeenCalledWith(pessoaCollection, ...additionalPessoas);
        expect(comp.pessoasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const idioma: IIdioma = { id: 456 };
        const pessoa: IPessoa = { id: 20567 };
        idioma.pessoa = pessoa;

        activatedRoute.data = of({ idioma });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(idioma));
        expect(comp.pessoasSharedCollection).toContain(pessoa);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const idioma = { id: 123 };
        spyOn(idiomaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ idioma });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: idioma }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(idiomaService.update).toHaveBeenCalledWith(idioma);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const idioma = new Idioma();
        spyOn(idiomaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ idioma });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: idioma }));
        saveSubject.complete();

        // THEN
        expect(idiomaService.create).toHaveBeenCalledWith(idioma);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const idioma = { id: 123 };
        spyOn(idiomaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ idioma });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(idiomaService.update).toHaveBeenCalledWith(idioma);
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
