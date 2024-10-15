import {MovementSpeed} from "./movement-speed";
import {StatBlock} from "./stat-block";
import {Characteristic} from "./characteristic";
import {Action} from "./action";
import {Alignment} from "./alignment";
import {ArmorClass} from "./armor-class";
import {Skill} from "./skill";

export class Creature {
  uuid?: string;
  name?: string;
  size?: string;
  type?: string;
  alignment?: Alignment;
  armorClass?: ArmorClass;
  hitPoints?: number;
  movementSpeed?: MovementSpeed;
  attributes?: StatBlock;
  savingThrowModifiers?: StatBlock;
  skills?: Skill[];
  damageImmunities?: string[];
  senses?: string[];
  languages?: string[];
  challengeRating?: number;
  characteristics?: Characteristic[];
  actions?: Action[];
  creatureDescription?: string;
  imageIds?: string[]
}
