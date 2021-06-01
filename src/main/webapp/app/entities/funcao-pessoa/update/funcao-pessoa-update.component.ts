import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFuncaoPessoa, FuncaoPessoa } from '../funcao-pessoa.model';
import { FuncaoPessoaService } from '../service/funcao-pessoa.service';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';

@Component({
  selector: 'jhi-funcao-pessoa-update',
  templateUrl: './funcao-pessoa-update.component.html',
})
export class FuncaoPessoaUpdateComponent implements OnInit {
  isSaving = false;

  pessoasSharedCollection: IPessoa[] = [];

  editForm = this.fb.group({
    id: [],
    funcao: [],
    criado: [null, [Validators.required]],
    ativo: [],
    pessoa: [],
  });

  constructor(
    protected funcaoPessoaService: FuncaoPessoaService,
    protected pessoaService: PessoaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcaoPessoa }) => {
      if (funcaoPessoa.id === undefined) {
        const today = dayjs().startOf('day');
        funcaoPessoa.criado = today;
      }

      this.updateForm(funcaoPessoa);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const funcaoPessoa = this.createFromForm();
    if (funcaoPessoa.id !== undefined) {
      this.subscribeToSaveResponse(this.funcaoPessoaService.update(funcaoPessoa));
    } else {
      this.subscribeToSaveResponse(this.funcaoPessoaService.create(funcaoPessoa));
    }
  }

  trackPessoaById(index: number, item: IPessoa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFuncaoPessoa>>): void {
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

  protected updateForm(funcaoPessoa: IFuncaoPessoa): void {
    this.editForm.patchValue({
      id: funcaoPessoa.id,
      funcao: funcaoPessoa.funcao,
      criado: funcaoPessoa.criado ? funcaoPessoa.criado.format(DATE_TIME_FORMAT) : null,
      ativo: funcaoPessoa.ativo,
      pessoa: funcaoPessoa.pessoa,
    });

    this.pessoasSharedCollection = this.pessoaService.addPessoaToCollectionIfMissing(this.pessoasSharedCollection, funcaoPessoa.pessoa);
  }

  protected loadRelationshipsOptions(): void {
    this.pessoaService
      .query()
      .pipe(map((res: HttpResponse<IPessoa[]>) => res.body ?? []))
      .pipe(map((pessoas: IPessoa[]) => this.pessoaService.addPessoaToCollectionIfMissing(pessoas, this.editForm.get('pessoa')!.value)))
      .subscribe((pessoas: IPessoa[]) => (this.pessoasSharedCollection = pessoas));
  }

  protected createFromForm(): IFuncaoPessoa {
    return {
      ...new FuncaoPessoa(),
      id: this.editForm.get(['id'])!.value,
      funcao: this.editForm.get(['funcao'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      ativo: this.editForm.get(['ativo'])!.value,
      pessoa: this.editForm.get(['pessoa'])!.value,
    };
  }
}
