import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVaga, Vaga } from '../vaga.model';
import { VagaService } from '../service/vaga.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { PessoaService } from 'app/entities/pessoa/service/pessoa.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';

@Component({
  selector: 'jhi-vaga-update',
  templateUrl: './vaga-update.component.html',
})
export class VagaUpdateComponent implements OnInit {
  isSaving = false;

  pessoasSharedCollection: IPessoa[] = [];
  empresasSharedCollection: IEmpresa[] = [];

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required]],
    titulo: [null, [Validators.required]],
    estagio: [],
    salario: [],
    beneficios: [],
    jornadaSemanal: [],
    bannerUrl: [],
    fonte: [],
    linkRecrutamento: [],
    ativo: [],
    preenchida: [],
    criado: [null, [Validators.required]],
    prazoAnuncio: [],
    cadastrou: [],
    empresa: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected vagaService: VagaService,
    protected pessoaService: PessoaService,
    protected empresaService: EmpresaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vaga }) => {
      if (vaga.id === undefined) {
        const today = dayjs().startOf('day');
        vaga.criado = today;
        vaga.prazoAnuncio = today;
      }

      this.updateForm(vaga);

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
    const vaga = this.createFromForm();
    if (vaga.id !== undefined) {
      this.subscribeToSaveResponse(this.vagaService.update(vaga));
    } else {
      this.subscribeToSaveResponse(this.vagaService.create(vaga));
    }
  }

  trackPessoaById(index: number, item: IPessoa): number {
    return item.id!;
  }

  trackEmpresaById(index: number, item: IEmpresa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVaga>>): void {
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

  protected updateForm(vaga: IVaga): void {
    this.editForm.patchValue({
      id: vaga.id,
      descricao: vaga.descricao,
      titulo: vaga.titulo,
      estagio: vaga.estagio,
      salario: vaga.salario,
      beneficios: vaga.beneficios,
      jornadaSemanal: vaga.jornadaSemanal,
      bannerUrl: vaga.bannerUrl,
      fonte: vaga.fonte,
      linkRecrutamento: vaga.linkRecrutamento,
      ativo: vaga.ativo,
      preenchida: vaga.preenchida,
      criado: vaga.criado ? vaga.criado.format(DATE_TIME_FORMAT) : null,
      prazoAnuncio: vaga.prazoAnuncio ? vaga.prazoAnuncio.format(DATE_TIME_FORMAT) : null,
      cadastrou: vaga.cadastrou,
      empresa: vaga.empresa,
    });

    this.pessoasSharedCollection = this.pessoaService.addPessoaToCollectionIfMissing(this.pessoasSharedCollection, vaga.cadastrou);
    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(this.empresasSharedCollection, vaga.empresa);
  }

  protected loadRelationshipsOptions(): void {
    this.pessoaService
      .query()
      .pipe(map((res: HttpResponse<IPessoa[]>) => res.body ?? []))
      .pipe(map((pessoas: IPessoa[]) => this.pessoaService.addPessoaToCollectionIfMissing(pessoas, this.editForm.get('cadastrou')!.value)))
      .subscribe((pessoas: IPessoa[]) => (this.pessoasSharedCollection = pessoas));

    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing(empresas, this.editForm.get('empresa')!.value))
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));
  }

  protected createFromForm(): IVaga {
    return {
      ...new Vaga(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      estagio: this.editForm.get(['estagio'])!.value,
      salario: this.editForm.get(['salario'])!.value,
      beneficios: this.editForm.get(['beneficios'])!.value,
      jornadaSemanal: this.editForm.get(['jornadaSemanal'])!.value,
      bannerUrl: this.editForm.get(['bannerUrl'])!.value,
      fonte: this.editForm.get(['fonte'])!.value,
      linkRecrutamento: this.editForm.get(['linkRecrutamento'])!.value,
      ativo: this.editForm.get(['ativo'])!.value,
      preenchida: this.editForm.get(['preenchida'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      prazoAnuncio: this.editForm.get(['prazoAnuncio'])!.value
        ? dayjs(this.editForm.get(['prazoAnuncio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      cadastrou: this.editForm.get(['cadastrou'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
    };
  }
}
