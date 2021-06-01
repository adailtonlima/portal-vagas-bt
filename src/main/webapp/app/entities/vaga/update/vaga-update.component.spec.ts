jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VagaService } from '../service/vaga.service';
import { IVaga, Vaga } from '../vaga.model';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';

import { VagaUpdateComponent } from './vaga-update.component';

describe('Component Tests', () => {
  describe('Vaga Management Update Component', () => {
    let comp: VagaUpdateComponent;
    let fixture: ComponentFixture<VagaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vagaService: VagaService;
    let pessoaService: PessoaService;
    let empresaService: EmpresaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VagaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VagaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VagaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vagaService = TestBed.inject(VagaService);
      pessoaService = TestBed.inject(PessoaService);
      empresaService = TestBed.inject(EmpresaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Pessoa query and add missing value', () => {
        const vaga: IVaga = { id: 456 };
        const cadastrou: IPessoa = { id: 68496 };
        vaga.cadastrou = cadastrou;

        const pessoaCollection: IPessoa[] = [{ id: 63464 }];
        spyOn(pessoaService, 'query').and.returnValue(of(new HttpResponse({ body: pessoaCollection })));
        const additionalPessoas = [cadastrou];
        const expectedCollection: IPessoa[] = [...additionalPessoas, ...pessoaCollection];
        spyOn(pessoaService, 'addPessoaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ vaga });
        comp.ngOnInit();

        expect(pessoaService.query).toHaveBeenCalled();
        expect(pessoaService.addPessoaToCollectionIfMissing).toHaveBeenCalledWith(pessoaCollection, ...additionalPessoas);
        expect(comp.pessoasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Empresa query and add missing value', () => {
        const vaga: IVaga = { id: 456 };
        const empresa: IEmpresa = { id: 39337 };
        vaga.empresa = empresa;

        const empresaCollection: IEmpresa[] = [{ id: 38594 }];
        spyOn(empresaService, 'query').and.returnValue(of(new HttpResponse({ body: empresaCollection })));
        const additionalEmpresas = [empresa];
        const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
        spyOn(empresaService, 'addEmpresaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ vaga });
        comp.ngOnInit();

        expect(empresaService.query).toHaveBeenCalled();
        expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(empresaCollection, ...additionalEmpresas);
        expect(comp.empresasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const vaga: IVaga = { id: 456 };
        const cadastrou: IPessoa = { id: 18561 };
        vaga.cadastrou = cadastrou;
        const empresa: IEmpresa = { id: 36861 };
        vaga.empresa = empresa;

        activatedRoute.data = of({ vaga });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vaga));
        expect(comp.pessoasSharedCollection).toContain(cadastrou);
        expect(comp.empresasSharedCollection).toContain(empresa);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const vaga = { id: 123 };
        spyOn(vagaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ vaga });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vaga }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(vagaService.update).toHaveBeenCalledWith(vaga);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const vaga = new Vaga();
        spyOn(vagaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ vaga });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vaga }));
        saveSubject.complete();

        // THEN
        expect(vagaService.create).toHaveBeenCalledWith(vaga);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const vaga = { id: 123 };
        spyOn(vagaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ vaga });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(vagaService.update).toHaveBeenCalledWith(vaga);
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

      describe('trackEmpresaById', () => {
        it('Should return tracked Empresa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEmpresaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
