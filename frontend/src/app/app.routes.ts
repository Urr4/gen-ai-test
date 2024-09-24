import {Routes} from '@angular/router';
import {MonsterPageComponent} from "./monster-page/monster-page.component";

export const routes: Routes = [
  {path: '', component: MonsterPageComponent},
  {path: 'monsters', component: MonsterPageComponent},
];
