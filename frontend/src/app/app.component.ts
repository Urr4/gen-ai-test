import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {MatDivider} from "@angular/material/divider";
import {NgOptimizedImage} from "@angular/common";
import {MonsterSearchFormComponent} from "./monster-search-form/monster-search-form.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgOptimizedImage, MatDivider, MonsterSearchFormComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
}
