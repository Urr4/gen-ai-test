import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateMonsterDialogComponent } from './create-monster-dialog.component';

describe('CreateMonsterDialogComponent', () => {
  let component: CreateMonsterDialogComponent;
  let fixture: ComponentFixture<CreateMonsterDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateMonsterDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateMonsterDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
