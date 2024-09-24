import {Component} from '@angular/core';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MonsterService} from "../monster.service";

@Component({
  selector: 'app-create-monster-dialog',
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
  templateUrl: './create-monster-dialog.component.html',
  styleUrl: './create-monster-dialog.component.scss'
})
export class CreateMonsterDialogComponent {

  createMonsterForm: FormGroup;

  constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<CreateMonsterDialogComponent>, private monsterService: MonsterService) {
    this.createMonsterForm = this.fb.group({
      prompt: [''],
      generateImage: false,
    });
  }

  generateMonster() {
    console.log("Generating monster")
    this.dialogRef.close(this.monsterService.generateNewMonster(this.createMonsterForm.value.prompt, this.createMonsterForm.value.generateImage));
  }

  abort() {
    this.dialogRef.close();
  }

}
