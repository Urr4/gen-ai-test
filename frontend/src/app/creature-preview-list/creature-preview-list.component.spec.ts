import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreaturePreviewListComponent } from './creature-preview-list.component';

describe('CreaturePreviewListComponent', () => {
  let component: CreaturePreviewListComponent;
  let fixture: ComponentFixture<CreaturePreviewListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreaturePreviewListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreaturePreviewListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
