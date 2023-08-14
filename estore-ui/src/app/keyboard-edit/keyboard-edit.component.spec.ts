import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyboardEditComponent } from './keyboard-edit.component';

describe('KeyboardEditComponent', () => {
  let component: KeyboardEditComponent;
  let fixture: ComponentFixture<KeyboardEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ KeyboardEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KeyboardEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
