import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAreaAtuacao } from '../area-atuacao.model';

@Component({
  selector: 'jhi-area-atuacao-detail',
  templateUrl: './area-atuacao-detail.component.html',
})
export class AreaAtuacaoDetailComponent implements OnInit {
  areaAtuacao: IAreaAtuacao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ areaAtuacao }) => {
      this.areaAtuacao = areaAtuacao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
