import {Component, OnInit} from '@angular/core';
import {MonsterSearchFormComponent} from "../monster-search-form/monster-search-form.component";
import {MonsterPreviewListComponent} from "../monster-preview-list/monster-preview-list.component";
import {MatButton} from "@angular/material/button";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CreateMonsterDialogComponent} from "../create-monster-dialog/create-monster-dialog.component";
import {MonsterDetailViewComponent} from "../monster-detail-view/monster-detail-view.component";
import {MatDivider} from "@angular/material/divider";
import {MonsterPreview} from "../models/monster/monster-preview";
import {MonsterService} from "../monster.service";
import {MonsterFilter} from "../models/monster/monster-filter";
import {Observable} from "rxjs";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {NgIf} from "@angular/common";
import {PageRequest} from "../models/page-request";
import {NotifyService} from "../notify.service";

@Component({
  selector: 'app-monster-page',
  standalone: true,
  imports: [
    MonsterSearchFormComponent,
    MonsterPreviewListComponent,
    MatButton,
    MonsterDetailViewComponent,
    MatDivider,
    MatProgressSpinner,
    NgIf
  ],
  templateUrl: './monster-page.component.html',
  styleUrl: './monster-page.component.scss'
})
export class MonsterPageComponent implements OnInit {

  currentFilter: MonsterFilter;
  monsterPreviews: MonsterPreview[] = [];
  totalNumberOfMatchingMonsters: number = 0;
  currentSelectedMonsterId?: string;
  isLoading: boolean = false;

  constructor(private dialog: MatDialog, private monsterService: MonsterService, private notifyService: NotifyService) {
    this.currentFilter = {armorClassRange: {}, hitPointsRange: {}}
  }

  ngOnInit(): void {
    this.loadMonsterByCurrentFilter();
  }

  openMonsterCreateDialog(): void {
    let dialogRef: MatDialogRef<CreateMonsterDialogComponent, Observable<MonsterPreview>> = this.dialog.open(CreateMonsterDialogComponent, {
      width: '700px',
    });
    dialogRef.afterClosed()
      .subscribe(monsterPreviewObservable => {
        if (monsterPreviewObservable != undefined) {
          this.isLoading = true;
          monsterPreviewObservable!.subscribe({
            next: data => {
              console.log("Generated monster", data.name);
              this.notifyService.notify("Generated new monster " + data.name);
              this.isLoading = false;
              this.currentSelectedMonsterId = data.uuid;
              this.loadMonsterByCurrentFilter();
            },
            error: err => {
              console.error("Failed to generate monster", err);
              this.notifyService.error("Could not generate new monster", err);
              this.isLoading = false;
            }
          })
        }
      })
  }

  updateFilter(monsterFilter: MonsterFilter): void {
    this.currentFilter = monsterFilter;
    this.loadMonsterByCurrentFilter();
  }

  updatePagination(pagination: PageRequest) {
    this.currentFilter.page = pagination.pageNumber;
    this.currentFilter.pageSize = pagination.pageSize;
    this.loadMonsterByCurrentFilter();
  }

  loadMonsterByCurrentFilter() {
    this.monsterService.getMonsterPreviewsByFilter(this.currentFilter)
      .subscribe({
        next: data => {
          console.log("Loaded previews", data);
          this.monsterPreviews = data.fetchedMonsterPreviews ? data.fetchedMonsterPreviews : [];
          this.totalNumberOfMatchingMonsters = data.totalNumberOfMatchingMonsterPreviews ? data.totalNumberOfMatchingMonsterPreviews : 0;
        },
        error: err => console.log("Could not load monster-previews", err)
      })
  }

  monsterSelected(uuid: string): void {
    this.currentSelectedMonsterId = uuid;
  }

  monsterDeleted(): void {
    this.currentSelectedMonsterId = undefined;
    this.loadMonsterByCurrentFilter();
  }

}
