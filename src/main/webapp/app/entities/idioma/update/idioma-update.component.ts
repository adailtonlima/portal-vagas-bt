import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IIdioma, Idioma } from '../idioma.model';
import { IdiomaService } from '../service/idioma.service';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';

@Component({
  selector: 'jhi-idioma-update',
  templateUrl: './idioma-update.component.html',
})
export class IdiomaUpdateComponent implements OnInit {
  isSaving = false;

  pessoasSharedCollection: IPessoa[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [],
    nivel: [],
    criado: [null, [Validators.required]],
    pessoa: [],
  });

  constructor(
    protected idiomaService: IdiomaService,
    protected pessoaService: PessoaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ idioma }) => {
      if (idioma.id === undefined) {
        const today = dayjs().startOf('day');
        idioma.criado = today;
      }

      this.updateForm(idioma);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const idioma = this.createFromForm();
    if (idioma.id !== undefined) {
      this.subscribeToSaveResponse(this.idiomaService.update(idioma));
    } else {
      this.subscribeToSaveResponse(this.idiomaService.create(idioma));
    }
  }

  trackPessoaById(index: number, item: IPessoa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIdioma>>): void {
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

  protected updateForm(idioma: IIdioma): void {
    this.editForm.patchValue({
      id: idioma.id,
      nome: idioma.nome,
      nivel: idioma.nivel,
      criado: idioma.criado ? idioma.criado.format(DATE_TIME_FORMAT) : null,
      pessoa: idioma.pessoa,
    });

    this.pessoasSharedCollection = this.pessoaService.addPessoaToCollectionIfMissing(this.pessoasSharedCollection, idioma.pessoa);
  }

  protected loadRelationshipsOptions(): void {
    this.pessoaService
      .query()
      .pipe(map((res: HttpResponse<IPessoa[]>) => res.body ?? []))
      .pipe(map((pessoas: IPessoa[]) => this.pessoaService.addPessoaToCollectionIfMissing(pessoas, this.editForm.get('pessoa')!.value)))
      .subscribe((pessoas: IPessoa[]) => (this.pessoasSharedCollection = pessoas));
  }

  protected createFromForm(): IIdioma {
    return {
      ...new Idioma(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      nivel: this.editForm.get(['nivel'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      pessoa: this.editForm.get(['pessoa'])!.value,
    };
  }
}
