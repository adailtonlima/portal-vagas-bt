import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FuncaoPessoaDetailComponent } from './funcao-pessoa-detail.component';

describe('Component Tests', () => {
  describe('FuncaoPessoa Management Detail Component', () => {
    let comp: FuncaoPessoaDetailComponent;
    let fixture: ComponentFixture<FuncaoPessoaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FuncaoPessoaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ funcaoPessoa: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FuncaoPessoaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FuncaoPessoaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load funcaoPessoa on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.funcaoPessoa).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
