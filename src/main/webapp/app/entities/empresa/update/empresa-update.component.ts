import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEmpresa, Empresa } from '../empresa.model';
import { EmpresaService } from '../service/empresa.service';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IAreaAtuacao } from 'app/entities/area-atuacao/area-atuacao.model';
import { AreaAtuacaoService } from 'app/entities/area-atuacao/service/area-atuacao.service';

@Component({
  selector: 'jhi-empresa-update',
  templateUrl: './empresa-update.component.html',
})
export class EmpresaUpdateComponent implements OnInit {
  isSaving = false;

  enderecosCollection: IEndereco[] = [];
  usersSharedCollection: IUser[] = [];
  areaAtuacaosSharedCollection: IAreaAtuacao[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    cnpj: [],
    porte: [],
    criado: [null, [Validators.required]],
    endereco: [],
    user: [],
    area: [],
  });

  constructor(
    protected empresaService: EmpresaService,
    protected enderecoService: EnderecoService,
    protected userService: UserService,
    protected areaAtuacaoService: AreaAtuacaoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empresa }) => {
      if (empresa.id === undefined) {
        const today = dayjs().startOf('day');
        empresa.criado = today;
      }

      this.updateForm(empresa);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empresa = this.createFromForm();
    if (empresa.id !== undefined) {
      this.subscribeToSaveResponse(this.empresaService.update(empresa));
    } else {
      this.subscribeToSaveResponse(this.empresaService.create(empresa));
    }
  }

  trackEnderecoById(index: number, item: IEndereco): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackAreaAtuacaoById(index: number, item: IAreaAtuacao): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpresa>>): void {
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

  protected updateForm(empresa: IEmpresa): void {
    this.editForm.patchValue({
      id: empresa.id,
      nome: empresa.nome,
      cnpj: empresa.cnpj,
      porte: empresa.porte,
      criado: empresa.criado ? empresa.criado.format(DATE_TIME_FORMAT) : null,
      endereco: empresa.endereco,
      user: empresa.user,
      area: empresa.area,
    });

    this.enderecosCollection = this.enderecoService.addEnderecoToCollectionIfMissing(this.enderecosCollection, empresa.endereco);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, empresa.user);
    this.areaAtuacaosSharedCollection = this.areaAtuacaoService.addAreaAtuacaoToCollectionIfMissing(
      this.areaAtuacaosSharedCollection,
      empresa.area
    );
  }

  protected loadRelationshipsOptions(): void {
    this.enderecoService
      .query({ 'empresaId.specified': 'false' })
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

    this.areaAtuacaoService
      .query()
      .pipe(map((res: HttpResponse<IAreaAtuacao[]>) => res.body ?? []))
      .pipe(
        map((areaAtuacaos: IAreaAtuacao[]) =>
          this.areaAtuacaoService.addAreaAtuacaoToCollectionIfMissing(areaAtuacaos, this.editForm.get('area')!.value)
        )
      )
      .subscribe((areaAtuacaos: IAreaAtuacao[]) => (this.areaAtuacaosSharedCollection = areaAtuacaos));
  }

  protected createFromForm(): IEmpresa {
    return {
      ...new Empresa(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      cnpj: this.editForm.get(['cnpj'])!.value,
      porte: this.editForm.get(['porte'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      endereco: this.editForm.get(['endereco'])!.value,
      user: this.editForm.get(['user'])!.value,
      area: this.editForm.get(['area'])!.value,
    };
  }
}
