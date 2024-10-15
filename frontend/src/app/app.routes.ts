import {Routes} from '@angular/router';
import {CreaturePageComponent} from "./creature-page/creature-page.component";

export const routes: Routes = [
  {path: '', component: CreaturePageComponent},
  {path: 'creatures', component: CreaturePageComponent},
];
