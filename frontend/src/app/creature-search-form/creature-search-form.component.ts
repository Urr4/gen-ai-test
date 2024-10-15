import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatSliderModule} from "@angular/material/slider";
import {MatSelectModule} from "@angular/material/select";
import {MatInputModule} from "@angular/material/input";
import {NgForOf, NgIf} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";
import {CreatureService} from "../creature.service";
import {CreatureFilter} from "../models/creature/creature-filter";

@Component({
  selector: 'app-creature-search-form',
  standalone: true,
  imports: [
    MatInputModule,
    MatSelectModule,
    MatSliderModule,
    FormsModule,
    ReactiveFormsModule,
    NgForOf,
    MatButtonModule,
    NgIf,
  ],
  templateUrl: './creature-search-form.component.html',
  styleUrl: './creature-search-form.component.scss'
})
export class CreatureSearchFormComponent implements OnInit {
  filterForm?: FormGroup;

  sizeOptions: string[] = [];
  typeOptions: string[] = [];
  selectableArmorClassRange: number[] = []
  selectableHitpointRange: number[] = []

  @Output() filterApplied = new EventEmitter<CreatureFilter>();

  constructor(private fb: FormBuilder, private creatureService: CreatureService) {}

  ngOnInit(): void {
    this.creatureService.getFilterParams()
      .subscribe({
        next: (data) => {
          if(data.currentTypes){
            this.typeOptions = ["-"];
            this.typeOptions.push(...data.currentTypes);
          }
          if(data.currentSizes){
            this.sizeOptions = ["-"];
            this.sizeOptions.push(...data.currentSizes);
          }
          if(data.armorClassRange){
            this.selectableArmorClassRange[0] = data.armorClassRange.start;
            this.selectableArmorClassRange[1] = data.armorClassRange.end;
          }
          if(data.hitPointRange){
            this.selectableHitpointRange.push(data.hitPointRange.start);
            this.selectableHitpointRange.push(data.hitPointRange.end);
          }
          this.filterForm = this.fb.group({
            nameIncludes: [''],
            size: [''],
            type: [''],
            armorClassFrom: this.selectableArmorClassRange[0],
            armorClassTo: this.selectableArmorClassRange[1],
            hitpointsFrom: this.selectableHitpointRange[0],
            hitpointsTo: this.selectableHitpointRange[1]
          });
        },
        error: (err) => console.error("Could not load types", err)
      });
  }

  applyFilter() {
    let creatureFilter = new CreatureFilter(
      this.filterForm!.value.nameIncludes,
      this.filterForm!.value.size === "-" ? null: this.filterForm!.value.size,
      this.filterForm!.value.type === "-" ? null: this.filterForm!.value.type,
      this.filterForm!.value.armorClassFrom,
      this.filterForm!.value.armorClassTo,
      this.filterForm!.value.hitpointsFrom,
      this.filterForm!.value.hitpointsTo,
    );
    this.filterApplied.emit(creatureFilter);
  }
}
