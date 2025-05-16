import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FullNameComponent } from './full-name.component';

describe('FullNameComponent', () => {
  let component: FullNameComponent;
  let fixture: ComponentFixture<FullNameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FullNameComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FullNameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
