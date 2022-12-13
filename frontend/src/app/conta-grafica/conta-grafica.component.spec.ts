import { Message } from './../shared/message';
import { of } from 'rxjs';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { DataService } from './../shared/services/data.service';
import { Router } from '@angular/router';
import { ContaGraficaService } from './service/conta-grafica.service';
import { ContaGraficaComponent } from './conta-grafica.component';

describe('ContaGraficaComponent', () => {
  let component: ContaGraficaComponent;
  let fixture: ComponentFixture<ContaGraficaComponent>;

  let message = new Message('');
  let logado = true;


  beforeEach(() => {
    const dataServiceStub = () => ({
      isLogado: () => (logado),
      setContaGrafica: (conta: string) => ({})
    });
    const routerStub = () => ({ navigateByUrl: (url: string) => ({}) });
    const contaGraficaServiceStub = () => ({
      entrar: (form: FormData) => (of(message))
    });
    TestBed.configureTestingModule({
      schemas: [NO_ERRORS_SCHEMA],
      declarations: [ContaGraficaComponent],
      providers: [
        { provide: DataService, useFactory: dataServiceStub },
        { provide: Router, useFactory: routerStub },
        { provide: ContaGraficaService, useFactory: contaGraficaServiceStub }
      ]
    });
    fixture = TestBed.createComponent(ContaGraficaComponent);
    component = fixture.componentInstance;
    component.ngOnInit();
  });

  it('can load instance', () => {
    expect(component).toBeTruthy();
  });

  it(`loading has default value`, () => {
    expect(component.loading).toEqual(false);
  });

  describe('submitConta', () => {
    it('makes expected calls', () => {
      const dataServiceStub: DataService = fixture.debugElement.injector.get(
        DataService
      );
      const routerStub: Router = fixture.debugElement.injector.get(Router);
      const contaGraficaServiceStub: ContaGraficaService = fixture.debugElement.injector.get(
        ContaGraficaService
      );
      
      spyOn(dataServiceStub, 'setContaGrafica').and.callThrough();
      spyOn(routerStub, 'navigateByUrl').and.callThrough();
      spyOn(contaGraficaServiceStub, 'entrar').and.callThrough();
      component.submitConta();
      expect(dataServiceStub.setContaGrafica).toHaveBeenCalled();
      expect(routerStub.navigateByUrl).toHaveBeenCalled();
      expect(contaGraficaServiceStub.entrar).toHaveBeenCalled();
    });
  });
});
