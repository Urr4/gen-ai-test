import {Damage} from "./damage";

export class Action {
  name?: string;
  rechargeOn?: string;
  kindOfAttack?: string;
  modifierToHit?: number;
  reach?: number;
  numberOfTargets?: string;
  description?: string;
  damages?: Damage[];
}
