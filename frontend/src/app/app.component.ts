import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {MatDivider} from "@angular/material/divider";
import {NgOptimizedImage} from "@angular/common";
import {CreatureSearchFormComponent} from "./creature-search-form/creature-search-form.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgOptimizedImage, MatDivider, CreatureSearchFormComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
}
