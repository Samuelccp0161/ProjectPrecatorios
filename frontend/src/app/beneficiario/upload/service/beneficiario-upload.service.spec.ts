import { TestBed } from '@angular/core/testing';

import { BeneficiarioUploadService } from './beneficiario-upload.service';

describe('BeneficiarioUploadService', () => {
  let service: BeneficiarioUploadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeneficiarioUploadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
