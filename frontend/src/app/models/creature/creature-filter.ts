export class CreatureFilter {
  nameIncludes?: string;
  size?: string;
  type?: string;
  armorClassRange: { from?: number; to?: number };
  hitPointsRange: { from?: number; to?: number };
  page?: number;
  pageSize?: number;

  constructor(
    nameIncludes: string,
    size: string,
    type: string,
    armorClassFrom: number,
    armorClassTo: number,
    hitPointsFrom: number,
    hitPointsTo: number,
  ) {
    this.nameIncludes = nameIncludes;
    this.size = size;
    this.type = type;
    this.armorClassRange = {from: armorClassFrom, to: armorClassTo};
    this.hitPointsRange = {from: hitPointsFrom, to: hitPointsTo};
  }
}
