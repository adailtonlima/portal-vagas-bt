import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIdioma } from '../idioma.model';

@Component({
  selector: 'jhi-idioma-detail',
  templateUrl: './idioma-detail.component.html',
})
export class IdiomaDetailComponent implements OnInit {
  idioma: IIdioma | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ idioma }) => {
      this.idioma = idioma;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
