import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVaga } from '../vaga.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-vaga-detail',
  templateUrl: './vaga-detail.component.html',
})
export class VagaDetailComponent implements OnInit {
  vaga: IVaga | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vaga }) => {
      this.vaga = vaga;
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
