import {Component} from '@angular/core';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {CreatureService} from "../creature.service";

@Component({
  selector: 'app-create-creature-dialog',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatCheckboxModule,
    MatDialogModule,
    MatButtonModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './create-creature-dialog.component.html',
  styleUrl: './create-creature-dialog.component.scss'
})
export class CreateCreatureDialogComponent {

  createCreatureForm: FormGroup;

  constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<CreateCreatureDialogComponent>, private creatureService: CreatureService) {
    this.createCreatureForm = this.fb.group({
      prompt: [''],
      generateImage: false,
    });
  }

  generateCreatures() {
    console.log("Generating creature")
    this.dialogRef.close(this.creatureService.generateNewCreature(this.createCreatureForm.value.prompt, this.createCreatureForm.value.generateImage));
  }

  abort() {
    this.dialogRef.close();
  }

}
