import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatCardModule} from "@angular/material/card";
import {NgForOf} from "@angular/common";
import {MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {MonsterPreview} from "../models/monster/monster-preview";
import {PageRequest} from "../models/page-request";
import {MatIconModule} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatMenuModule} from "@angular/material/menu";
import {MonsterService} from "../monster.service";
import {NotifyService} from "../notify.service";

@Component({
  selector: 'app-monster-preview-list',
  standalone: true,
  imports: [
    MatCardModule,
    NgForOf,
    MatPaginatorModule,
    MatIconModule,
    MatIconButton,
    MatMenuModule
  ],
  templateUrl: './monster-preview-list.component.html',
  styleUrl: './monster-preview-list.component.scss'
})
export class MonsterPreviewListComponent implements OnInit {

  @Input() monsterPreviews: MonsterPreview[] = [];
  @Input() totalNumberOfElements: number = 0;

  @Output() monsterSelected: EventEmitter<string> = new EventEmitter<string>();
  @Output() paginationUpdated: EventEmitter<PageRequest> = new EventEmitter<PageRequest>();
  @Output() monsterDeleted: EventEmitter<void> = new EventEmitter<void>();

  paginatedMonsters = this.monsterPreviews;
  pageSize = 10;
  currentPage = 0;
  pageSizeOptions = [5, 10, 20];

  constructor(private monsterService: MonsterService, private notifyService: NotifyService) {
  }

  ngOnInit() {
    this.paginationUpdated.emit(new PageRequest(this.currentPage, this.pageSize));
  }

  paginateElements() {
    const startIndex = this.currentPage * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedMonsters = this.monsterPreviews.slice(startIndex, endIndex);
  }

  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.paginationUpdated.emit(new PageRequest(this.currentPage, this.pageSize));
    this.paginateElements();
  }

  onMonsterSelected(monsterPreview: any) {
    this.monsterSelected.emit(monsterPreview.uuid);
  }

  deleteMonster(monsterId: string) {
    this.monsterService.deleteMonster(monsterId)
      .subscribe({
        complete: () => this.monsterDeleted.emit(),
        error: err => this.notifyService.error("Could not delete monster", err)
      });
  }

}
