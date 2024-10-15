export class CreaturePreview {
  uuid: string;
  name: string;
  shortHand: string;
  description: string;
  armorClass: number;
  hitPoints: number;

  constructor(
    uuid: string,
    name: string,
    shortHand: string,
    description: string,
    armorClass: number,
    hitPoints: number) {
    this.uuid = uuid;
    this.name = name;
    this.shortHand = shortHand;
    this.description = description;
    this.armorClass = armorClass;
    this.hitPoints = hitPoints;
  }
}
