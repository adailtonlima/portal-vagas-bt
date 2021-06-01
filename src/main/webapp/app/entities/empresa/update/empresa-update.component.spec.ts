jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EmpresaService } from '../service/empresa.service';
import { IEmpresa, Empresa } from '../empresa.model';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IAreaAtuacao } from 'app/entities/area-atuacao/area-atuacao.model';
import { AreaAtuacaoService } from 'app/entities/area-atuacao/service/area-atuacao.service';

import { EmpresaUpdateComponent } from './empresa-update.component';

describe('Component Tests', () => {
  describe('Empresa Management Update Component', () => {
    let comp: EmpresaUpdateComponent;
    let fixture: ComponentFixture<EmpresaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let empresaService: EmpresaService;
    let enderecoService: EnderecoService;
    let userService: UserService;
    let areaAtuacaoService: AreaAtuacaoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmpresaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EmpresaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmpresaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      empresaService = TestBed.inject(EmpresaService);
      enderecoService = TestBed.inject(EnderecoService);
      userService = TestBed.inject(UserService);
      areaAtuacaoService = TestBed.inject(AreaAtuacaoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call endereco query and add missing value', () => {
        const empresa: IEmpresa = { id: 456 };
        const endereco: IEndereco = { id: 75490 };
        empresa.endereco = endereco;

        const enderecoCollection: IEndereco[] = [{ id: 89945 }];
        spyOn(enderecoService, 'query').and.returnValue(of(new HttpResponse({ body: enderecoCollection })));
        const expectedCollection: IEndereco[] = [endereco, ...enderecoCollection];
        spyOn(enderecoService, 'addEnderecoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        expect(enderecoService.query).toHaveBeenCalled();
        expect(enderecoService.addEnderecoToCollectionIfMissing).toHaveBeenCalledWith(enderecoCollection, endereco);
        expect(comp.enderecosCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const empresa: IEmpresa = { id: 456 };
        const user: IUser = { id: 27699 };
        empresa.user = user;

        const userCollection: IUser[] = [{ id: 87926 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call AreaAtuacao query and add missing value', () => {
        const empresa: IEmpresa = { id: 456 };
        const area: IAreaAtuacao = { id: 60166 };
        empresa.area = area;

        const areaAtuacaoCollection: IAreaAtuacao[] = [{ id: 69575 }];
        spyOn(areaAtuacaoService, 'query').and.returnValue(of(new HttpResponse({ body: areaAtuacaoCollection })));
        const additionalAreaAtuacaos = [area];
        const expectedCollection: IAreaAtuacao[] = [...additionalAreaAtuacaos, ...areaAtuacaoCollection];
        spyOn(areaAtuacaoService, 'addAreaAtuacaoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        expect(areaAtuacaoService.query).toHaveBeenCalled();
        expect(areaAtuacaoService.addAreaAtuacaoToCollectionIfMissing).toHaveBeenCalledWith(
          areaAtuacaoCollection,
          ...additionalAreaAtuacaos
        );
        expect(comp.areaAtuacaosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const empresa: IEmpresa = { id: 456 };
        const endereco: IEndereco = { id: 77235 };
        empresa.endereco = endereco;
        const user: IUser = { id: 47918 };
        empresa.user = user;
        const area: IAreaAtuacao = { id: 79703 };
        empresa.area = area;

        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(empresa));
        expect(comp.enderecosCollection).toContain(endereco);
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.areaAtuacaosSharedCollection).toContain(area);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const empresa = { id: 123 };
        spyOn(empresaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: empresa }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(empresaService.update).toHaveBeenCalledWith(empresa);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const empresa = new Empresa();
        spyOn(empresaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: empresa }));
        saveSubject.complete();

        // THEN
        expect(empresaService.create).toHaveBeenCalledWith(empresa);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const empresa = { id: 123 };
        spyOn(empresaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ empresa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(empresaService.update).toHaveBeenCalledWith(empresa);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEnderecoById', () => {
        it('Should return tracked Endereco primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEnderecoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAreaAtuacaoById', () => {
        it('Should return tracked AreaAtuacao primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAreaAtuacaoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
