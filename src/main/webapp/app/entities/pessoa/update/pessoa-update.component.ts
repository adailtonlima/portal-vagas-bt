import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPessoa, Pessoa } from '../pessoa.model';
import { PessoaService } from '../service/pessoa.service';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-pessoa-update',
  templateUrl: './pessoa-update.component.html',
})
export class PessoaUpdateComponent implements OnInit {
  isSaving = false;

  enderecosCollection: IEndereco[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    email: [],
    dataNascimento: [],
    cpf: [],
    telefone: [],
    nacionalidade: [],
    naturalidade: [],
    sexo: [],
    estadoCivil: [],
    pcd: [],
    pcdCID: [],
    cnh: [],
    fotoUrl: [],
    criado: [null, [Validators.required]],
    endereco: [],
    user: [],
  });

  constructor(
    protected pessoaService: PessoaService,
    protected enderecoService: EnderecoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pessoa }) => {
      if (pessoa.id === undefined) {
        const today = dayjs().startOf('day');
        pessoa.criado = today;
      }

      this.updateForm(pessoa);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pessoa = this.createFromForm();
    if (pessoa.id !== undefined) {
      this.subscribeToSaveResponse(this.pessoaService.update(pessoa));
    } else {
      this.subscribeToSaveResponse(this.pessoaService.create(pessoa));
    }
  }

  trackEnderecoById(index: number, item: IEndereco): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPessoa>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pessoa: IPessoa): void {
    this.editForm.patchValue({
      id: pessoa.id,
      nome: pessoa.nome,
      email: pessoa.email,
      dataNascimento: pessoa.dataNascimento,
      cpf: pessoa.cpf,
      telefone: pessoa.telefone,
      nacionalidade: pessoa.nacionalidade,
      naturalidade: pessoa.naturalidade,
      sexo: pessoa.sexo,
      estadoCivil: pessoa.estadoCivil,
      pcd: pessoa.pcd,
      pcdCID: pessoa.pcdCID,
      cnh: pessoa.cnh,
      fotoUrl: pessoa.fotoUrl,
      criado: pessoa.criado ? pessoa.criado.format(DATE_TIME_FORMAT) : null,
      endereco: pessoa.endereco,
      user: pessoa.user,
    });

    this.enderecosCollection = this.enderecoService.addEnderecoToCollectionIfMissing(this.enderecosCollection, pessoa.endereco);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, pessoa.user);
  }

  protected loadRelationshipsOptions(): void {
    this.enderecoService
      .query({ 'pessoaId.specified': 'false' })
      .pipe(map((res: HttpResponse<IEndereco[]>) => res.body ?? []))
      .pipe(
        map((enderecos: IEndereco[]) =>
          this.enderecoService.addEnderecoToCollectionIfMissing(enderecos, this.editForm.get('endereco')!.value)
        )
      )
      .subscribe((enderecos: IEndereco[]) => (this.enderecosCollection = enderecos));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPessoa {
    return {
      ...new Pessoa(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      email: this.editForm.get(['email'])!.value,
      dataNascimento: this.editForm.get(['dataNascimento'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      telefone: this.editForm.get(['telefone'])!.value,
      nacionalidade: this.editForm.get(['nacionalidade'])!.value,
      naturalidade: this.editForm.get(['naturalidade'])!.value,
      sexo: this.editForm.get(['sexo'])!.value,
      estadoCivil: this.editForm.get(['estadoCivil'])!.value,
      pcd: this.editForm.get(['pcd'])!.value,
      pcdCID: this.editForm.get(['pcdCID'])!.value,
      cnh: this.editForm.get(['cnh'])!.value,
      fotoUrl: this.editForm.get(['fotoUrl'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      endereco: this.editForm.get(['endereco'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
