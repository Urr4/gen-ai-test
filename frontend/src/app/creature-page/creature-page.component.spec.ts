import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreaturePageComponent } from './creature-page.component';

describe('CreaturePageComponent', () => {
  let component: CreaturePageComponent;
  let fixture: ComponentFixture<CreaturePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreaturePageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreaturePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
