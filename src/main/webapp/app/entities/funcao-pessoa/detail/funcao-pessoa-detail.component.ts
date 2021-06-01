import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFuncaoPessoa } from '../funcao-pessoa.model';

@Component({
  selector: 'jhi-funcao-pessoa-detail',
  templateUrl: './funcao-pessoa-detail.component.html',
})
export class FuncaoPessoaDetailComponent implements OnInit {
  funcaoPessoa: IFuncaoPessoa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcaoPessoa }) => {
      this.funcaoPessoa = funcaoPessoa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
