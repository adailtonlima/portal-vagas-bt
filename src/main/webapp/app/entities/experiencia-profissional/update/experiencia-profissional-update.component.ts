import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IExperienciaProfissional, ExperienciaProfissional } from '../experiencia-profissional.model';
import { ExperienciaProfissionalService } from '../service/experiencia-profissional.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';

@Component({
  selector: 'jhi-experiencia-profissional-update',
  templateUrl: './experiencia-profissional-update.component.html',
})
export class ExperienciaProfissionalUpdateComponent implements OnInit {
  isSaving = false;

  pessoasSharedCollection: IPessoa[] = [];

  editForm = this.fb.group({
    id: [],
    empresa: [],
    cargo: [],
    segmento: [],
    porte: [],
    inicio: [],
    fim: [],
    descricaoAtividade: [],
    salario: [],
    beneficios: [],
    criado: [null, [Validators.required]],
    pessoa: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected experienciaProfissionalService: ExperienciaProfissionalService,
    protected pessoaService: PessoaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ experienciaProfissional }) => {
      if (experienciaProfissional.id === undefined) {
        const today = dayjs().startOf('day');
        experienciaProfissional.criado = today;
      }

      this.updateForm(experienciaProfissional);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('portalVagasEmpregoApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const experienciaProfissional = this.createFromForm();
    if (experienciaProfissional.id !== undefined) {
      this.subscribeToSaveResponse(this.experienciaProfissionalService.update(experienciaProfissional));
    } else {
      this.subscribeToSaveResponse(this.experienciaProfissionalService.create(experienciaProfissional));
    }
  }

  trackPessoaById(index: number, item: IPessoa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExperienciaProfissional>>): void {
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

  protected updateForm(experienciaProfissional: IExperienciaProfissional): void {
    this.editForm.patchValue({
      id: experienciaProfissional.id,
      empresa: experienciaProfissional.empresa,
      cargo: experienciaProfissional.cargo,
      segmento: experienciaProfissional.segmento,
      porte: experienciaProfissional.porte,
      inicio: experienciaProfissional.inicio,
      fim: experienciaProfissional.fim,
      descricaoAtividade: experienciaProfissional.descricaoAtividade,
      salario: experienciaProfissional.salario,
      beneficios: experienciaProfissional.beneficios,
      criado: experienciaProfissional.criado ? experienciaProfissional.criado.format(DATE_TIME_FORMAT) : null,
      pessoa: experienciaProfissional.pessoa,
    });

    this.pessoasSharedCollection = this.pessoaService.addPessoaToCollectionIfMissing(
      this.pessoasSharedCollection,
      experienciaProfissional.pessoa
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pessoaService
      .query()
      .pipe(map((res: HttpResponse<IPessoa[]>) => res.body ?? []))
      .pipe(map((pessoas: IPessoa[]) => this.pessoaService.addPessoaToCollectionIfMissing(pessoas, this.editForm.get('pessoa')!.value)))
      .subscribe((pessoas: IPessoa[]) => (this.pessoasSharedCollection = pessoas));
  }

  protected createFromForm(): IExperienciaProfissional {
    return {
      ...new ExperienciaProfissional(),
      id: this.editForm.get(['id'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
      cargo: this.editForm.get(['cargo'])!.value,
      segmento: this.editForm.get(['segmento'])!.value,
      porte: this.editForm.get(['porte'])!.value,
      inicio: this.editForm.get(['inicio'])!.value,
      fim: this.editForm.get(['fim'])!.value,
      descricaoAtividade: this.editForm.get(['descricaoAtividade'])!.value,
      salario: this.editForm.get(['salario'])!.value,
      beneficios: this.editForm.get(['beneficios'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      pessoa: this.editForm.get(['pessoa'])!.value,
    };
  }
}
