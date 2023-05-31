import { TestBed } from '@angular/core/testing';

import { TributarioUploadService } from './tributario-upload.service';

describe('UploadService', () => {
  let service: TributarioUploadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TributarioUploadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
