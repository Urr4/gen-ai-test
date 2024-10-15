import {Component, OnInit} from '@angular/core';
import {CreatureSearchFormComponent} from "../creature-search-form/creature-search-form.component";
import {CreaturePreviewListComponent} from "../creature-preview-list/creature-preview-list.component";
import {MatButton} from "@angular/material/button";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CreateCreatureDialogComponent} from "../create-creature-dialog/create-creature-dialog.component";
import {CreatureDetailViewComponent} from "../creature-detail-view/creature-detail-view.component";
import {MatDivider} from "@angular/material/divider";
import {CreaturePreview} from "../models/creature/creature-preview";
import {CreatureService} from "../creature.service";
import {CreatureFilter} from "../models/creature/creature-filter";
import {Observable} from "rxjs";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {NgIf} from "@angular/common";
import {PageRequest} from "../models/page-request";
import {NotifyService} from "../notify.service";

@Component({
  selector: 'app-creature-page',
  standalone: true,
  imports: [
    CreatureSearchFormComponent,
    CreaturePreviewListComponent,
    MatButton,
    CreatureDetailViewComponent,
    MatDivider,
    MatProgressSpinner,
    NgIf
  ],
  templateUrl: './creature-page.component.html',
  styleUrl: './creature-page.component.scss'
})
export class CreaturePageComponent implements OnInit {

  currentFilter: CreatureFilter;
  creaturePreviews: CreaturePreview[] = [];
  totalNumberOfMatchingCreatures: number = 0;
  currentSelectedCreatureId?: string;
  isLoading: boolean = false;

  constructor(private dialog: MatDialog, private creatureService: CreatureService, private notifyService: NotifyService) {
    this.currentFilter = {armorClassRange: {}, hitPointsRange: {}}
  }

  ngOnInit(): void {
    this.loadCreatureByCurrentFilter();
  }

  openCreatureCreateDialog(): void {
    let dialogRef: MatDialogRef<CreateCreatureDialogComponent, Observable<CreaturePreview>> = this.dialog.open(CreateCreatureDialogComponent, {
      width: '700px',
    });
    dialogRef.afterClosed()
      .subscribe(CreaturePreviewObservable => {
        if (CreaturePreviewObservable != undefined) {
          this.isLoading = true;
          CreaturePreviewObservable!.subscribe({
            next: data => {
              console.log("Generated creature", data.name);
              this.notifyService.notify("Generated new creature " + data.name);
              this.isLoading = false;
              this.currentSelectedCreatureId = data.uuid;
              this.loadCreatureByCurrentFilter();
            },
            error: err => {
              console.error("Failed to generate creature", err);
              this.notifyService.error("Could not generate new creature", err);
              this.isLoading = false;
            }
          })
        }
      })
  }

  updateFilter(creatureFilter: CreatureFilter): void {
    this.currentFilter = creatureFilter;
    this.loadCreatureByCurrentFilter();
  }

  updatePagination(pagination: PageRequest) {
    this.currentFilter.page = pagination.pageNumber;
    this.currentFilter.pageSize = pagination.pageSize;
    this.loadCreatureByCurrentFilter();
  }

  loadCreatureByCurrentFilter() {
    this.creatureService.getCreaturePreviewsByFilter(this.currentFilter)
      .subscribe({
        next: data => {
          console.log("Loaded previews", data);
          this.creaturePreviews = data.fetchedCreaturePreviews ? data.fetchedCreaturePreviews : [];
          this.totalNumberOfMatchingCreatures = data.totalNumberOfMatchingCreaturePreviews ? data.totalNumberOfMatchingCreaturePreviews : 0;
        },
        error: err => console.log("Could not load creature-previews", err)
      })
  }

  creatureSelected(uuid: string): void {
    this.currentSelectedCreatureId = uuid;
  }

  creatureDeleted(): void {
    this.currentSelectedCreatureId = undefined;
    this.loadCreatureByCurrentFilter();
  }

}
