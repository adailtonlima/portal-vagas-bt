import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPerfilProfissional, PerfilProfissional } from '../perfil-profissional.model';
import { PerfilProfissionalService } from '../service/perfil-profissional.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAreaAtuacao } from 'app/entities/area-atuacao/area-atuacao.model';
import { AreaAtuacaoService } from 'app/entities/area-atuacao/service/area-atuacao.service';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';

@Component({
  selector: 'jhi-perfil-profissional-update',
  templateUrl: './perfil-profissional-update.component.html',
})
export class PerfilProfissionalUpdateComponent implements OnInit {
  isSaving = false;

  areaAtuacaosSharedCollection: IAreaAtuacao[] = [];
  pessoasSharedCollection: IPessoa[] = [];

  editForm = this.fb.group({
    id: [],
    estagio: [],
    procurandoEmprego: [],
    objetivosPessoais: [],
    pretensaoSalarial: [],
    criado: [null, [Validators.required]],
    area: [],
    pessoa: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected perfilProfissionalService: PerfilProfissionalService,
    protected areaAtuacaoService: AreaAtuacaoService,
    protected pessoaService: PessoaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ perfilProfissional }) => {
      if (perfilProfissional.id === undefined) {
        const today = dayjs().startOf('day');
        perfilProfissional.criado = today;
      }

      this.updateForm(perfilProfissional);

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
    const perfilProfissional = this.createFromForm();
    if (perfilProfissional.id !== undefined) {
      this.subscribeToSaveResponse(this.perfilProfissionalService.update(perfilProfissional));
    } else {
      this.subscribeToSaveResponse(this.perfilProfissionalService.create(perfilProfissional));
    }
  }

  trackAreaAtuacaoById(index: number, item: IAreaAtuacao): number {
    return item.id!;
  }

  trackPessoaById(index: number, item: IPessoa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerfilProfissional>>): void {
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

  protected updateForm(perfilProfissional: IPerfilProfissional): void {
    this.editForm.patchValue({
      id: perfilProfissional.id,
      estagio: perfilProfissional.estagio,
      procurandoEmprego: perfilProfissional.procurandoEmprego,
      objetivosPessoais: perfilProfissional.objetivosPessoais,
      pretensaoSalarial: perfilProfissional.pretensaoSalarial,
      criado: perfilProfissional.criado ? perfilProfissional.criado.format(DATE_TIME_FORMAT) : null,
      area: perfilProfissional.area,
      pessoa: perfilProfissional.pessoa,
    });

    this.areaAtuacaosSharedCollection = this.areaAtuacaoService.addAreaAtuacaoToCollectionIfMissing(
      this.areaAtuacaosSharedCollection,
      perfilProfissional.area
    );
    this.pessoasSharedCollection = this.pessoaService.addPessoaToCollectionIfMissing(
      this.pessoasSharedCollection,
      perfilProfissional.pessoa
    );
  }

  protected loadRelationshipsOptions(): void {
    this.areaAtuacaoService
      .query()
      .pipe(map((res: HttpResponse<IAreaAtuacao[]>) => res.body ?? []))
      .pipe(
        map((areaAtuacaos: IAreaAtuacao[]) =>
          this.areaAtuacaoService.addAreaAtuacaoToCollectionIfMissing(areaAtuacaos, this.editForm.get('area')!.value)
        )
      )
      .subscribe((areaAtuacaos: IAreaAtuacao[]) => (this.areaAtuacaosSharedCollection = areaAtuacaos));

    this.pessoaService
      .query()
      .pipe(map((res: HttpResponse<IPessoa[]>) => res.body ?? []))
      .pipe(map((pessoas: IPessoa[]) => this.pessoaService.addPessoaToCollectionIfMissing(pessoas, this.editForm.get('pessoa')!.value)))
      .subscribe((pessoas: IPessoa[]) => (this.pessoasSharedCollection = pessoas));
  }

  protected createFromForm(): IPerfilProfissional {
    return {
      ...new PerfilProfissional(),
      id: this.editForm.get(['id'])!.value,
      estagio: this.editForm.get(['estagio'])!.value,
      procurandoEmprego: this.editForm.get(['procurandoEmprego'])!.value,
      objetivosPessoais: this.editForm.get(['objetivosPessoais'])!.value,
      pretensaoSalarial: this.editForm.get(['pretensaoSalarial'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      area: this.editForm.get(['area'])!.value,
      pessoa: this.editForm.get(['pessoa'])!.value,
    };
  }
}
