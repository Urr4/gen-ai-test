import {CreaturePreview} from "./creature-preview";

export class CreaturePreviewPageResponse {
  fetchedCreaturePreviews?: CreaturePreview[];
  totalNumberOfMatchingCreaturePreviews?: number;
}
