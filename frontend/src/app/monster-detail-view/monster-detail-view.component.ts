import {Component, Input, OnChanges} from '@angular/core';
import {MatDivider} from "@angular/material/divider";
import {LowerCasePipe, NgForOf, NgIf} from "@angular/common";
import {MonsterService} from "../monster.service";
import {Monster} from "../models/monster/monster";
import {MonsterImageRendererComponent} from "../monster-image-renderer/monster-image-renderer.component";
import {MatButton} from "@angular/material/button";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Observable} from "rxjs";
import {CreateImageDialogComponent} from "../create-image-dialog/create-image-dialog.component";
import {NotifyService} from "../notify.service";

@Component({
  selector: 'app-monster-detail-view',
  standalone: true,
  imports: [
    MatDivider,
    NgForOf,
    NgIf,
    LowerCasePipe,
    MonsterImageRendererComponent,
    MatButton,
    MatProgressSpinner
  ],
  templateUrl: './monster-detail-view.component.html',
  styleUrl: './monster-detail-view.component.scss'
})
export class MonsterDetailViewComponent implements OnChanges {

  @Input() monsterId?: string;
  monster?: Monster;
  isLoading: boolean = false;
  currentImageId?: string;

  constructor(private dialog: MatDialog, private monsterService: MonsterService, private notifyService: NotifyService) {
  }

  ngOnChanges() {
    if (this.monsterId) {
      this.loadMonster(this.monsterId);
    }else{
      this.monster = undefined;
    }
  }

  private loadMonster(monsterId: string) {
    this.isLoading = true;
    this.monsterService.getMonsterById(monsterId)
      .subscribe({
        next: data => {
          console.log("Loaded monster", data)
          this.monster = data;
          this.isLoading = false;
        },
        error: err => {
          console.error("Could not load monster", this.monsterId, err);
          this.isLoading = false;
          this.notifyService.error("Could not load monster", err);
        }
      })
  }

  openCreateImageDialog() {
    let dialogRef: MatDialogRef<CreateImageDialogComponent, Observable<string>> = this.dialog.open(CreateImageDialogComponent, {
      width: '400px',
      data: {
        monsterId: this.monsterId
      }
    });
    dialogRef.afterClosed()
      .subscribe(imageGenerationObservable => {
        if (imageGenerationObservable != undefined) {
          this.isLoading = true;
          imageGenerationObservable!.subscribe({
            next: imageId => {
              this.notifyService.notify("Generated new image for monster " + this.monster?.name);
              this.isLoading = false;
            },
            error: err => {
              console.error("Failed to generate image", err);
              this.notifyService.error("Could not generate image for monster " + this.monster?.name, err);
              this.isLoading = false;
            }
          })
        }
      })
  }

  updateCurrentImageId(imageId: string) {
    this.currentImageId = imageId;
  }

  deleteImage() {
    console.log("Deleting", this.currentImageId);
    this.monsterService.deleteImageFromMonster(this.monsterId!, this.currentImageId!)
      .subscribe({
        complete: () => {
          this.notifyService.notify("Delete image")
          this.loadMonster(this.monsterId!);
        },
        error: err => {
          console.error("Failed to remote image", this.currentImageId, err);
          this.notifyService.error("Could not delete image", err);
        }
      });
  }

}
