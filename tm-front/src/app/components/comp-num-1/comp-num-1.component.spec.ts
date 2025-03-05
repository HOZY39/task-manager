import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompNum1Component } from './comp-num-1.component';

describe('CompNum1Component', () => {
  let component: CompNum1Component;
  let fixture: ComponentFixture<CompNum1Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompNum1Component]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompNum1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
