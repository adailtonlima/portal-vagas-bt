jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PessoaService } from '../service/pessoa.service';
import { IPessoa, Pessoa } from '../pessoa.model';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { PessoaUpdateComponent } from './pessoa-update.component';

describe('Component Tests', () => {
  describe('Pessoa Management Update Component', () => {
    let comp: PessoaUpdateComponent;
    let fixture: ComponentFixture<PessoaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pessoaService: PessoaService;
    let enderecoService: EnderecoService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PessoaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PessoaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PessoaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pessoaService = TestBed.inject(PessoaService);
      enderecoService = TestBed.inject(EnderecoService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call endereco query and add missing value', () => {
        const pessoa: IPessoa = { id: 456 };
        const endereco: IEndereco = { id: 20281 };
        pessoa.endereco = endereco;

        const enderecoCollection: IEndereco[] = [{ id: 46691 }];
        spyOn(enderecoService, 'query').and.returnValue(of(new HttpResponse({ body: enderecoCollection })));
        const expectedCollection: IEndereco[] = [endereco, ...enderecoCollection];
        spyOn(enderecoService, 'addEnderecoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pessoa });
        comp.ngOnInit();

        expect(enderecoService.query).toHaveBeenCalled();
        expect(enderecoService.addEnderecoToCollectionIfMissing).toHaveBeenCalledWith(enderecoCollection, endereco);
        expect(comp.enderecosCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const pessoa: IPessoa = { id: 456 };
        const user: IUser = { id: 13820 };
        pessoa.user = user;

        const userCollection: IUser[] = [{ id: 8136 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pessoa });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pessoa: IPessoa = { id: 456 };
        const endereco: IEndereco = { id: 49572 };
        pessoa.endereco = endereco;
        const user: IUser = { id: 62330 };
        pessoa.user = user;

        activatedRoute.data = of({ pessoa });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pessoa));
        expect(comp.enderecosCollection).toContain(endereco);
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pessoa = { id: 123 };
        spyOn(pessoaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pessoa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pessoa }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pessoaService.update).toHaveBeenCalledWith(pessoa);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pessoa = new Pessoa();
        spyOn(pessoaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pessoa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pessoa }));
        saveSubject.complete();

        // THEN
        expect(pessoaService.create).toHaveBeenCalledWith(pessoa);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pessoa = { id: 123 };
        spyOn(pessoaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pessoa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pessoaService.update).toHaveBeenCalledWith(pessoa);
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
    });
  });
});
