import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TributarioComponent } from './tributario.component';

describe('TributarioComponent', () => {
  let component: TributarioComponent;
  let fixture: ComponentFixture<TributarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TributarioComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TributarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
