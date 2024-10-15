import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {CreatureFilter} from "./models/creature/creature-filter";
import {CreaturePreview} from "./models/creature/creature-preview";
import {Creature} from "./models/creature/creature";
import {FilterParams} from "./models/filter-params";
import {CreaturePreviewPageResponse} from "./models/creature/creature-preview-page-response";

@Injectable({
  providedIn: 'root'
})
export class CreatureService {

  private url = "http://localhost:8080/creatures";

  constructor(private http: HttpClient) {
  }

  getFilterParams(): Observable<FilterParams> {
    return this.http.get<FilterParams>(this.url + "/filterParams");
  }

  getCreaturePreviewsByFilter(filter?: CreatureFilter): Observable<CreaturePreviewPageResponse> {
    if (filter) {
      let params = new HttpParams();
      if (filter.nameIncludes != null && filter.nameIncludes.trim() != '') {
        params = params.set("nameIncludes", filter.nameIncludes)
      }
      if (filter.size != null && filter.size.trim() != '') {
        params = params.set("size", filter.size)
      }
      if (filter.type != null && filter.type.trim() != '') {
        params = params.set('type', filter.type)
      }
      if (filter.armorClassRange.from != null) {
        params = params.set("armorClassFrom", filter.armorClassRange.from)
      }
      if (filter.armorClassRange.to != null) {
        params = params.set("armorClassTo", filter.armorClassRange.to)
      }
      if (filter.hitPointsRange.from != null) {
        params = params.set("hitPointsFrom", filter.hitPointsRange.from)
      }
      if (filter.hitPointsRange.to != null) {
        params = params.set("hitPointsTo", filter.hitPointsRange.to)
      }
      if (filter.page != null) {
        params = params.set("page", filter.page)
      }
      if (filter.pageSize != null) {
        params = params.set("pageSize", filter.pageSize)
      }
      return this.http.get<CreaturePreviewPageResponse>(this.url, {params});
    } else {
      return this.http.get<CreaturePreviewPageResponse>(this.url);
    }
  }

  getCreatureById(id: string): Observable<Creature> {
    return this.http.get<Creature>(this.url + "/" + id);
  }

  generateNewImageForCreatureWithId(id: string): Observable<string> {
    console.log("Creating image for creature", id);
    return this.http.put<string>(this.url + "/" + id + "/image", {});
  }

  generateNewCreature(prompt: string, generateImage: boolean): Observable<CreaturePreview> {
    return this.http.post<CreaturePreview>(this.url + "/generate", {prompt: prompt, generateImage: generateImage});
  }

  deleteCreature(creatureId: string): Observable<void> {
    return this.http.delete<void>(this.url + "/" + creatureId);
  }

  deleteImageFromCreature(creatureId: string, imageId: string): Observable<void> {
    return this.http.delete<void>(this.url + "/" + creatureId + "/image/" + imageId);
  }
}
