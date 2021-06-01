import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAreaAtuacao, AreaAtuacao } from '../area-atuacao.model';
import { AreaAtuacaoService } from '../service/area-atuacao.service';

@Component({
  selector: 'jhi-area-atuacao-update',
  templateUrl: './area-atuacao-update.component.html',
})
export class AreaAtuacaoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
    descricao: [],
    iconeUrl: [],
    ativo: [],
    criado: [null, [Validators.required]],
  });

  constructor(protected areaAtuacaoService: AreaAtuacaoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ areaAtuacao }) => {
      if (areaAtuacao.id === undefined) {
        const today = dayjs().startOf('day');
        areaAtuacao.criado = today;
      }

      this.updateForm(areaAtuacao);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const areaAtuacao = this.createFromForm();
    if (areaAtuacao.id !== undefined) {
      this.subscribeToSaveResponse(this.areaAtuacaoService.update(areaAtuacao));
    } else {
      this.subscribeToSaveResponse(this.areaAtuacaoService.create(areaAtuacao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAreaAtuacao>>): void {
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

  protected updateForm(areaAtuacao: IAreaAtuacao): void {
    this.editForm.patchValue({
      id: areaAtuacao.id,
      nome: areaAtuacao.nome,
      descricao: areaAtuacao.descricao,
      iconeUrl: areaAtuacao.iconeUrl,
      ativo: areaAtuacao.ativo,
      criado: areaAtuacao.criado ? areaAtuacao.criado.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IAreaAtuacao {
    return {
      ...new AreaAtuacao(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      iconeUrl: this.editForm.get(['iconeUrl'])!.value,
      ativo: this.editForm.get(['ativo'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
