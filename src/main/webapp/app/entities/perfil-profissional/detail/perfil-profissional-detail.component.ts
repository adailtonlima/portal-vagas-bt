import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPerfilProfissional } from '../perfil-profissional.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-perfil-profissional-detail',
  templateUrl: './perfil-profissional-detail.component.html',
})
export class PerfilProfissionalDetailComponent implements OnInit {
  perfilProfissional: IPerfilProfissional | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ perfilProfissional }) => {
      this.perfilProfissional = perfilProfissional;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
