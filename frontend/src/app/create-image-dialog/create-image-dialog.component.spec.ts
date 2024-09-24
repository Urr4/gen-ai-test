import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateImageDialogComponent } from './create-image-dialog.component';

describe('CreateImageDialogComponent', () => {
  let component: CreateImageDialogComponent;
  let fixture: ComponentFixture<CreateImageDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateImageDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateImageDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
