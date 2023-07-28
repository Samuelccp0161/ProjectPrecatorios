import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BeneficiarioUploadComponent } from './beneficiario-upload.component';

describe('BeneficiarioUploadComponent', () => {
  let component: BeneficiarioUploadComponent;
  let fixture: ComponentFixture<BeneficiarioUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BeneficiarioUploadComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BeneficiarioUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
