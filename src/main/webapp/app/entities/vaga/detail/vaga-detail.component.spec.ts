import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { VagaDetailComponent } from './vaga-detail.component';

describe('Component Tests', () => {
  describe('Vaga Management Detail Component', () => {
    let comp: VagaDetailComponent;
    let fixture: ComponentFixture<VagaDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VagaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ vaga: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VagaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VagaDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load vaga on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vaga).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
