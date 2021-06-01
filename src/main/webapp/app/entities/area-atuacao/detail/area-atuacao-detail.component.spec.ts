import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AreaAtuacaoDetailComponent } from './area-atuacao-detail.component';

describe('Component Tests', () => {
  describe('AreaAtuacao Management Detail Component', () => {
    let comp: AreaAtuacaoDetailComponent;
    let fixture: ComponentFixture<AreaAtuacaoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AreaAtuacaoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ areaAtuacao: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AreaAtuacaoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AreaAtuacaoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load areaAtuacao on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.areaAtuacao).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
