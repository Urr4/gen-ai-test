import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonsterPreviewListComponent } from './monster-preview-list.component';

describe('MonsterPreviewListComponent', () => {
  let component: MonsterPreviewListComponent;
  let fixture: ComponentFixture<MonsterPreviewListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonsterPreviewListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonsterPreviewListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
