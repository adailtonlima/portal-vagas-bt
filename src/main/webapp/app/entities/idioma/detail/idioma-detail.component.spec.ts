import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IdiomaDetailComponent } from './idioma-detail.component';

describe('Component Tests', () => {
  describe('Idioma Management Detail Component', () => {
    let comp: IdiomaDetailComponent;
    let fixture: ComponentFixture<IdiomaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IdiomaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ idioma: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IdiomaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IdiomaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load idioma on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.idioma).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
