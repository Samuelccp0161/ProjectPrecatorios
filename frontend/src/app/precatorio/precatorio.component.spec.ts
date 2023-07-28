import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrecatorioComponent } from './precatorio.component';

describe('PrecatorioComponent', () => {
  let component: PrecatorioComponent;
  let fixture: ComponentFixture<PrecatorioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrecatorioComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PrecatorioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
