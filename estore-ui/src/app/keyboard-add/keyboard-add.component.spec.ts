import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyboardAddComponent } from './keyboard-add.component';

describe('KeyboardAddComponent', () => {
  let component: KeyboardAddComponent;
  let fixture: ComponentFixture<KeyboardAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ KeyboardAddComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KeyboardAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
