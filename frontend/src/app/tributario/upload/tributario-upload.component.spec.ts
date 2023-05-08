import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TributarioUploadComponent } from './tributario-upload.component';

describe('UploadComponent', () => {
  let component: TributarioUploadComponent;
  let fixture: ComponentFixture<TributarioUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TributarioUploadComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TributarioUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
