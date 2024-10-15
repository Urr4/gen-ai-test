import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatureDetailViewComponent } from './creature-detail-view.component';

describe('CreatureDetailViewComponent', () => {
  let component: CreatureDetailViewComponent;
  let fixture: ComponentFixture<CreatureDetailViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreatureDetailViewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreatureDetailViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
