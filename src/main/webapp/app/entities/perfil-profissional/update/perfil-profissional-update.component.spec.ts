jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PerfilProfissionalService } from '../service/perfil-profissional.service';
import { IPerfilProfissional, PerfilProfissional } from '../perfil-profissional.model';
import { IAreaAtuacao } from 'app/entities/area-atuacao/area-atuacao.model';
import { AreaAtuacaoService } from 'app/entities/area-atuacao/service/area-atuacao.service';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';

import { PerfilProfissionalUpdateComponent } from './perfil-profissional-update.component';

describe('Component Tests', () => {
  describe('PerfilProfissional Management Update Component', () => {
    let comp: PerfilProfissionalUpdateComponent;
    let fixture: ComponentFixture<PerfilProfissionalUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let perfilProfissionalService: PerfilProfissionalService;
    let areaAtuacaoService: AreaAtuacaoService;
    let pessoaService: PessoaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PerfilProfissionalUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PerfilProfissionalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PerfilProfissionalUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      perfilProfissionalService = TestBed.inject(PerfilProfissionalService);
      areaAtuacaoService = TestBed.inject(AreaAtuacaoService);
      pessoaService = TestBed.inject(PessoaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AreaAtuacao query and add missing value', () => {
        const perfilProfissional: IPerfilProfissional = { id: 456 };
        const area: IAreaAtuacao = { id: 11166 };
        perfilProfissional.area = area;

        const areaAtuacaoCollection: IAreaAtuacao[] = [{ id: 71754 }];
        spyOn(areaAtuacaoService, 'query').and.returnValue(of(new HttpResponse({ body: areaAtuacaoCollection })));
        const additionalAreaAtuacaos = [area];
        const expectedCollection: IAreaAtuacao[] = [...additionalAreaAtuacaos, ...areaAtuacaoCollection];
        spyOn(areaAtuacaoService, 'addAreaAtuacaoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ perfilProfissional });
        comp.ngOnInit();

        expect(areaAtuacaoService.query).toHaveBeenCalled();
        expect(areaAtuacaoService.addAreaAtuacaoToCollectionIfMissing).toHaveBeenCalledWith(
          areaAtuacaoCollection,
          ...additionalAreaAtuacaos
        );
        expect(comp.areaAtuacaosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Pessoa query and add missing value', () => {
        const perfilProfissional: IPerfilProfissional = { id: 456 };
        const pessoa: IPessoa = { id: 72519 };
        perfilProfissional.pessoa = pessoa;

        const pessoaCollection: IPessoa[] = [{ id: 63683 }];
        spyOn(pessoaService, 'query').and.returnValue(of(new HttpResponse({ body: pessoaCollection })));
        const additionalPessoas = [pessoa];
        const expectedCollection: IPessoa[] = [...additionalPessoas, ...pessoaCollection];
        spyOn(pessoaService, 'addPessoaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ perfilProfissional });
        comp.ngOnInit();

        expect(pessoaService.query).toHaveBeenCalled();
        expect(pessoaService.addPessoaToCollectionIfMissing).toHaveBeenCalledWith(pessoaCollection, ...additionalPessoas);
        expect(comp.pessoasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const perfilProfissional: IPerfilProfissional = { id: 456 };
        const area: IAreaAtuacao = { id: 54477 };
        perfilProfissional.area = area;
        const pessoa: IPessoa = { id: 9351 };
        perfilProfissional.pessoa = pessoa;

        activatedRoute.data = of({ perfilProfissional });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(perfilProfissional));
        expect(comp.areaAtuacaosSharedCollection).toContain(area);
        expect(comp.pessoasSharedCollection).toContain(pessoa);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const perfilProfissional = { id: 123 };
        spyOn(perfilProfissionalService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ perfilProfissional });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: perfilProfissional }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(perfilProfissionalService.update).toHaveBeenCalledWith(perfilProfissional);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const perfilProfissional = new PerfilProfissional();
        spyOn(perfilProfissionalService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ perfilProfissional });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: perfilProfissional }));
        saveSubject.complete();

        // THEN
        expect(perfilProfissionalService.create).toHaveBeenCalledWith(perfilProfissional);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const perfilProfissional = { id: 123 };
        spyOn(perfilProfissionalService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ perfilProfissional });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(perfilProfissionalService.update).toHaveBeenCalledWith(perfilProfissional);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAreaAtuacaoById', () => {
        it('Should return tracked AreaAtuacao primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAreaAtuacaoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
