import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonsterSearchFormComponent } from './monster-search-form.component';

describe('MonsterSearchFormComponent', () => {
  let component: MonsterSearchFormComponent;
  let fixture: ComponentFixture<MonsterSearchFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonsterSearchFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonsterSearchFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
