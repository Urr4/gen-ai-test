import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonsterPageComponent } from './monster-page.component';

describe('MonsterPageComponent', () => {
  let component: MonsterPageComponent;
  let fixture: ComponentFixture<MonsterPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonsterPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonsterPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
