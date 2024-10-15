import {Component, Input, OnChanges} from '@angular/core';
import {MatDivider} from "@angular/material/divider";
import {LowerCasePipe, NgForOf, NgIf} from "@angular/common";
import {CreatureService} from "../creature.service";
import {Creature} from "../models/creature/creature";
import {ImageRendererComponent} from "../image-renderer/image-renderer.component";
import {MatButton} from "@angular/material/button";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Observable} from "rxjs";
import {CreateImageDialogComponent} from "../create-image-dialog/create-image-dialog.component";
import {NotifyService} from "../notify.service";

@Component({
  selector: 'app-creature-detail-view',
  standalone: true,
  imports: [
    MatDivider,
    NgForOf,
    NgIf,
    LowerCasePipe,
    ImageRendererComponent,
    MatButton,
    MatProgressSpinner
  ],
  templateUrl: './creature-detail-view.component.html',
  styleUrl: './creature-detail-view.component.scss'
})
export class CreatureDetailViewComponent implements OnChanges {

  @Input() creatureId?: string;
  creature?: Creature;
  isLoading: boolean = false;
  currentImageId?: string;

  constructor(private dialog: MatDialog, private creatureService: CreatureService, private notifyService: NotifyService) {
  }

  ngOnChanges() {
    if (this.creatureId) {
      this.loadCreature(this.creatureId);
    }else{
      this.creature = undefined;
    }
  }

  private loadCreature(creatureId: string) {
    this.isLoading = true;
    this.creatureService.getCreatureById(creatureId)
      .subscribe({
        next: data => {
          console.log("Loaded creature", data)
          this.creature = data;
          this.isLoading = false;
        },
        error: err => {
          console.error("Could not load creature", this.creatureId, err);
          this.isLoading = false;
          this.notifyService.error("Could not load creature", err);
        }
      })
  }

  openCreateImageDialog() {
    let dialogRef: MatDialogRef<CreateImageDialogComponent, Observable<string>> = this.dialog.open(CreateImageDialogComponent, {
      width: '400px',
      data: {
        creatureId: this.creatureId
      }
    });
    dialogRef.afterClosed()
      .subscribe(imageGenerationObservable => {
        if (imageGenerationObservable != undefined) {
          this.isLoading = true;
          imageGenerationObservable!.subscribe({
            next: imageId => {
              this.notifyService.notify("Generated new image for creature " + this.creature?.name);
              this.isLoading = false;
            },
            error: err => {
              console.error("Failed to generate image", err);
              this.notifyService.error("Could not generate image for creature " + this.creature?.name, err);
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
    this.creatureService.deleteImageFromCreature(this.creatureId!, this.currentImageId!)
      .subscribe({
        complete: () => {
          this.notifyService.notify("Delete image")
          this.loadCreature(this.creatureId!);
        },
        error: err => {
          console.error("Failed to remote image", this.currentImageId, err);
          this.notifyService.error("Could not delete image", err);
        }
      });
  }

}
