import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageRendererComponent } from './image-renderer.component';

describe('CreatureImageRendererComponent', () => {
  let component: ImageRendererComponent;
  let fixture: ComponentFixture<ImageRendererComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImageRendererComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ImageRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
