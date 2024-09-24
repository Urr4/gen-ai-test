import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MonsterService} from "../monster.service";

@Component({
  selector: 'app-create-image-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatButtonModule
  ],
  templateUrl: './create-image-dialog.component.html',
  styleUrl: './create-image-dialog.component.scss'
})
export class CreateImageDialogComponent {

  constructor(private dialogRef: MatDialogRef<CreateImageDialogComponent>, private monsterService: MonsterService, @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  generateImage() {
    this.dialogRef.close(this.monsterService.generateNewImageForMonsterWithId(this.data.monsterId));
  }

  abort(){
    this.dialogRef.close();
  }

}
