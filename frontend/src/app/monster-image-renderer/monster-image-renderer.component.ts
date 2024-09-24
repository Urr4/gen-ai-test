import {Component, EventEmitter, Input, OnChanges, Output} from '@angular/core';
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {ImageService} from "../image.service";
import {NotifyService} from "../notify.service";
import {forkJoin, Observable} from "rxjs";
import {ImageWrapper} from "../models/image/image-wrapper";

@Component({
  selector: 'app-monster-image-renderer',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage,
    NgIf,
    MatIconModule,
    MatIconButton
  ],
  templateUrl: './monster-image-renderer.component.html',
  styleUrl: './monster-image-renderer.component.scss'
})
export class MonsterImageRendererComponent implements OnChanges {
  @Input() imageIds?: string[] = [];
  @Output() currentlyShowImageId: EventEmitter<string> = new EventEmitter<string>();

  imageIdByIndex: Map<number, string> = new Map();

  imageWrappers: ImageWrapper[] = [];

  currentIndex: number = 0;
  observables: Observable<ImageWrapper>[] = [];

  constructor(private imageService: ImageService, private notifyService: NotifyService) {
  }

  ngOnChanges(): void {
    this.observables = [];
    this.imageWrappers.splice(0, this.imageWrappers.length);
    this.currentIndex = 0;
    if (this.imageIds) {
      this.imageIds!.forEach(uuid => this.observables.push(this.imageService.getImageById(uuid)));
      let index = 0;
      forkJoin(this.observables).subscribe({
        next: imageWrappers => imageWrappers.forEach(wrapper => {
          this.imageWrappers.push(wrapper);
          this.imageIdByIndex.set(index++, wrapper.imageId);
        }),
        error: err => {
          console.log("Failed to load images", err);
          this.notifyService.error("Could not load images", err);
        },
        complete: () => {
          this.currentlyShowImageId.emit(this.imageIdByIndex.get(0))
        }
      })
    }
  }

  goToNext(): void {
    this.currentIndex = (this.currentIndex + 1) % this.imageWrappers.length;
    this.currentlyShowImageId.emit(this.imageIdByIndex.get(this.currentIndex));
  }

  goToPrev(): void {
    this.currentIndex = (this.currentIndex - 1 + this.imageWrappers.length) % this.imageWrappers.length;
    this.currentlyShowImageId.emit(this.imageIdByIndex.get(this.currentIndex));
  }

}
