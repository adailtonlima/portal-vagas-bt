import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormacaoDetailComponent } from './formacao-detail.component';

describe('Component Tests', () => {
  describe('Formacao Management Detail Component', () => {
    let comp: FormacaoDetailComponent;
    let fixture: ComponentFixture<FormacaoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FormacaoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ formacao: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FormacaoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FormacaoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load formacao on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.formacao).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
