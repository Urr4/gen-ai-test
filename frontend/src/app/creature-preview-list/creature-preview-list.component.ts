import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatCardModule} from "@angular/material/card";
import {NgForOf} from "@angular/common";
import {MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {CreaturePreview} from "../models/creature/creature-preview";
import {PageRequest} from "../models/page-request";
import {MatIconModule} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatMenuModule} from "@angular/material/menu";
import {CreatureService} from "../creature.service";
import {NotifyService} from "../notify.service";

@Component({
  selector: 'app-creature-preview-list',
  standalone: true,
  imports: [
    MatCardModule,
    NgForOf,
    MatPaginatorModule,
    MatIconModule,
    MatIconButton,
    MatMenuModule
  ],
  templateUrl: './creature-preview-list.component.html',
  styleUrl: './creature-preview-list.component.scss'
})
export class CreaturePreviewListComponent implements OnInit {

  @Input() creaturePreviews: CreaturePreview[] = [];
  @Input() totalNumberOfElements: number = 0;

  @Output() creatureSelected: EventEmitter<string> = new EventEmitter<string>();
  @Output() paginationUpdated: EventEmitter<PageRequest> = new EventEmitter<PageRequest>();
  @Output() creatureDeleted: EventEmitter<void> = new EventEmitter<void>();

  paginatedCreatures = this.creaturePreviews;
  pageSize = 10;
  currentPage = 0;
  pageSizeOptions = [5, 10, 20];

  constructor(private creatureService: CreatureService, private notifyService: NotifyService) {
  }

  ngOnInit() {
    this.paginationUpdated.emit(new PageRequest(this.currentPage, this.pageSize));
  }

  paginateElements() {
    const startIndex = this.currentPage * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedCreatures = this.creaturePreviews.slice(startIndex, endIndex);
  }

  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.paginationUpdated.emit(new PageRequest(this.currentPage, this.pageSize));
    this.paginateElements();
  }

  onCreatureSelected(creaturePreview: any) {
    this.creatureSelected.emit(creaturePreview.uuid);
  }

  deleteCreature(creatureId: string) {
    this.creatureService.deleteCreature(creatureId)
      .subscribe({
        complete: () => this.creatureDeleted.emit(),
        error: err => this.notifyService.error("Could not delete creature", err)
      });
  }

}
