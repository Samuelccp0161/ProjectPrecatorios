import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { ContaGraficaService } from './conta-grafica.service';

describe('ContaGraficaService', () => {
  let service: ContaGraficaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ContaGraficaService]
    });
    service = TestBed.inject(ContaGraficaService);
  });

  it('can load instance', () => {
    expect(service).toBeTruthy();
  });

  it(`api has default value`, () => {
    expect(service.api).toEqual(`/api/conta`);
  });
});
