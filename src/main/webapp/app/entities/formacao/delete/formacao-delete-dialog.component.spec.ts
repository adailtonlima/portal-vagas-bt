jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FormacaoService } from '../service/formacao.service';

import { FormacaoDeleteDialogComponent } from './formacao-delete-dialog.component';

describe('Component Tests', () => {
  describe('Formacao Management Delete Component', () => {
    let comp: FormacaoDeleteDialogComponent;
    let fixture: ComponentFixture<FormacaoDeleteDialogComponent>;
    let service: FormacaoService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FormacaoDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(FormacaoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FormacaoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FormacaoService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
