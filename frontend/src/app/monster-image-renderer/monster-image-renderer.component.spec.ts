import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonsterImageRendererComponent } from './monster-image-renderer.component';

describe('MonsterImageRendererComponent', () => {
  let component: MonsterImageRendererComponent;
  let fixture: ComponentFixture<MonsterImageRendererComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonsterImageRendererComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonsterImageRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
