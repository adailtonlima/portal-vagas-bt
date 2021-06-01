import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormacao } from '../formacao.model';

@Component({
  selector: 'jhi-formacao-detail',
  templateUrl: './formacao-detail.component.html',
})
export class FormacaoDetailComponent implements OnInit {
  formacao: IFormacao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formacao }) => {
      this.formacao = formacao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
