import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderHistoryViewComponent } from './order-history-view.component';

describe('OrderHistoryViewComponent', () => {
  let component: OrderHistoryViewComponent;
  let fixture: ComponentFixture<OrderHistoryViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderHistoryViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderHistoryViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
