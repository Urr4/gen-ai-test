import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCreatureDialogComponent } from './create-creature-dialog.component';

describe('CreateCreatureDialogComponent', () => {
  let component: CreateCreatureDialogComponent;
  let fixture: ComponentFixture<CreateCreatureDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateCreatureDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateCreatureDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
