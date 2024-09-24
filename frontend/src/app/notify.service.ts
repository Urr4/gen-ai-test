import {inject, Injectable} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class NotifyService {

  private _snackBar = inject(MatSnackBar);

  notify(message: string) {
    let action = "Ok";
    this._snackBar.open(message, action, {
      duration: 2000
    });
  }

  error(message: string, err: any) {
    let action = "Ok";
    this._snackBar.open(message + ": " + err, action, {
      duration: 2000,
      panelClass: ["error"]
    });
  }
}
