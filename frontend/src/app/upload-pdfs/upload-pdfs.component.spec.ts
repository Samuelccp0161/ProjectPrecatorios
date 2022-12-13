import { By } from '@angular/platform-browser';
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { Router } from "@angular/router";

import { DataService } from "../shared/services/data.service";
import { UploadService } from "./service/upload.service";
import { UploadPdfsComponent } from "./upload-pdfs.component";
import { Type } from '@angular/core';

describe("UploadPdfsComponent", () => {
  let component: UploadPdfsComponent;
  let fixture: ComponentFixture<UploadPdfsComponent>;

  let uploadServiceSpy: jasmine.SpyObj<UploadService>;
  let dataServiceSpy: jasmine.SpyObj<DataService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    uploadServiceSpy = jasmine.createSpyObj("UploadService", ["submeter", "upload"]);
    dataServiceSpy = jasmine.createSpyObj("DataService", ["getContaGrafica"]);
    routerSpy = jasmine.createSpyObj("Router", ["navigateByUrl"]);

    await TestBed.configureTestingModule({
      declarations: [UploadPdfsComponent],
      providers: [
        { provide: UploadService, useValue: uploadServiceSpy },
        { provide: DataService, useValue: dataServiceSpy },
        { provide: Router, useValue: routerSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UploadPdfsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  describe('Initial state', () => {
    it("should create", () => {
      expect(component).toBeTruthy();
    });

    it("should return to /grafica if conta is empty", () => {
      dataServiceSpy.getContaGrafica.and.returnValue('');
      component.ngOnInit();

      expect(routerSpy.navigateByUrl).toHaveBeenCalledWith('/grafica');
    })

    it('should get the conta grafica value', () => {
      const conta = '512';
      dataServiceSpy.getContaGrafica.and.returnValue(conta);
      component.ngOnInit();

      expect(component.contaGrafica).toBe(conta);
    })
  })
});
