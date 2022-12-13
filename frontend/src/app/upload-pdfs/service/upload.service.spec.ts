import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { Message } from 'src/app/shared/message';

import { UploadService } from './upload.service';

describe("UploadService", () => {
  let service: UploadService;
  let spyHttp: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    spyHttp = jasmine.createSpyObj("HttpClient", ["post"]);
    service = new UploadService(spyHttp);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });

  describe("On upload", () => {
    it("should send the file and return the response", () => {
      const expectedMsg = new Message("123");
      const form = new FormData();

      spyHttp.post.and.returnValue(of(expectedMsg));

      service.upload(form).subscribe((msg) => {
        expect(msg).withContext("expected message").toEqual(expectedMsg);
      });

      expect(spyHttp.post).toHaveBeenCalledWith(service.apiUpload, form);
    });
  });

  describe("On Submit", () => {
    it("should send the file and return the response", () => {
      let expectedMsg = new Message("ops");

      spyHttp.post.and.returnValue(of(expectedMsg));

      service.submeter().subscribe((msg) => {
        expect(msg).withContext("expected message").toEqual(expectedMsg);
      });

      expect(spyHttp.post).toHaveBeenCalledWith(service.apiSubmit, {});
    });
  });
});
