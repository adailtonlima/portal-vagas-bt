import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFormacao, Formacao } from '../formacao.model';
import { FormacaoService } from '../service/formacao.service';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';

@Component({
  selector: 'jhi-formacao-update',
  templateUrl: './formacao-update.component.html',
})
export class FormacaoUpdateComponent implements OnInit {
  isSaving = false;

  pessoasSharedCollection: IPessoa[] = [];

  editForm = this.fb.group({
    id: [],
    instituicao: [],
    tipo: [],
    inicio: [],
    conclusao: [],
    criado: [null, [Validators.required]],
    pessoa: [],
  });

  constructor(
    protected formacaoService: FormacaoService,
    protected pessoaService: PessoaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formacao }) => {
      if (formacao.id === undefined) {
        const today = dayjs().startOf('day');
        formacao.criado = today;
      }

      this.updateForm(formacao);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formacao = this.createFromForm();
    if (formacao.id !== undefined) {
      this.subscribeToSaveResponse(this.formacaoService.update(formacao));
    } else {
      this.subscribeToSaveResponse(this.formacaoService.create(formacao));
    }
  }

  trackPessoaById(index: number, item: IPessoa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormacao>>): void {
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

  protected updateForm(formacao: IFormacao): void {
    this.editForm.patchValue({
      id: formacao.id,
      instituicao: formacao.instituicao,
      tipo: formacao.tipo,
      inicio: formacao.inicio,
      conclusao: formacao.conclusao,
      criado: formacao.criado ? formacao.criado.format(DATE_TIME_FORMAT) : null,
      pessoa: formacao.pessoa,
    });

    this.pessoasSharedCollection = this.pessoaService.addPessoaToCollectionIfMissing(this.pessoasSharedCollection, formacao.pessoa);
  }

  protected loadRelationshipsOptions(): void {
    this.pessoaService
      .query()
      .pipe(map((res: HttpResponse<IPessoa[]>) => res.body ?? []))
      .pipe(map((pessoas: IPessoa[]) => this.pessoaService.addPessoaToCollectionIfMissing(pessoas, this.editForm.get('pessoa')!.value)))
      .subscribe((pessoas: IPessoa[]) => (this.pessoasSharedCollection = pessoas));
  }

  protected createFromForm(): IFormacao {
    return {
      ...new Formacao(),
      id: this.editForm.get(['id'])!.value,
      instituicao: this.editForm.get(['instituicao'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      inicio: this.editForm.get(['inicio'])!.value,
      conclusao: this.editForm.get(['conclusao'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      pessoa: this.editForm.get(['pessoa'])!.value,
    };
  }
}
