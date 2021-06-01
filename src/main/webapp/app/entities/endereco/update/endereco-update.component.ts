import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEndereco, Endereco } from '../endereco.model';
import { EnderecoService } from '../service/endereco.service';

@Component({
  selector: 'jhi-endereco-update',
  templateUrl: './endereco-update.component.html',
})
export class EnderecoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cep: [null, [Validators.required]],
    logradouro: [],
    complemento: [],
    numero: [],
    uf: [],
    municipio: [],
    bairro: [],
    latitude: [],
    longitude: [],
    criado: [null, [Validators.required]],
  });

  constructor(protected enderecoService: EnderecoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ endereco }) => {
      if (endereco.id === undefined) {
        const today = dayjs().startOf('day');
        endereco.criado = today;
      }

      this.updateForm(endereco);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const endereco = this.createFromForm();
    if (endereco.id !== undefined) {
      this.subscribeToSaveResponse(this.enderecoService.update(endereco));
    } else {
      this.subscribeToSaveResponse(this.enderecoService.create(endereco));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEndereco>>): void {
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

  protected updateForm(endereco: IEndereco): void {
    this.editForm.patchValue({
      id: endereco.id,
      cep: endereco.cep,
      logradouro: endereco.logradouro,
      complemento: endereco.complemento,
      numero: endereco.numero,
      uf: endereco.uf,
      municipio: endereco.municipio,
      bairro: endereco.bairro,
      latitude: endereco.latitude,
      longitude: endereco.longitude,
      criado: endereco.criado ? endereco.criado.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IEndereco {
    return {
      ...new Endereco(),
      id: this.editForm.get(['id'])!.value,
      cep: this.editForm.get(['cep'])!.value,
      logradouro: this.editForm.get(['logradouro'])!.value,
      complemento: this.editForm.get(['complemento'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      uf: this.editForm.get(['uf'])!.value,
      municipio: this.editForm.get(['municipio'])!.value,
      bairro: this.editForm.get(['bairro'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
