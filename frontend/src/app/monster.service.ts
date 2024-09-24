import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {MonsterFilter} from "./models/monster/monster-filter";
import {MonsterPreview} from "./models/monster/monster-preview";
import {Monster} from "./models/monster/monster";
import {FilterParams} from "./models/filter-params";
import {MonsterPreviewPageResponse} from "./models/monster/monster-preview-page-response";

@Injectable({
  providedIn: 'root'
})
export class MonsterService {

  private url = "http://localhost:8080/monsters";

  constructor(private http: HttpClient) {
  }

  getFilterParams(): Observable<FilterParams> {
    return this.http.get<FilterParams>(this.url + "/filterParams");
  }

  getMonsterPreviewsByFilter(filter?: MonsterFilter): Observable<MonsterPreviewPageResponse> {
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
      return this.http.get<MonsterPreviewPageResponse>(this.url, {params});
    } else {
      return this.http.get<MonsterPreviewPageResponse>(this.url);
    }
  }

  getMonsterById(id: string): Observable<Monster> {
    return this.http.get<Monster>(this.url + "/" + id);
  }

  generateNewImageForMonsterWithId(id: string): Observable<string> {
    console.log("Creating image for monster", id);
    return this.http.put<string>(this.url + "/" + id + "/image", {});
  }

  generateNewMonster(prompt: string, generateImage: boolean): Observable<MonsterPreview> {
    return this.http.post<MonsterPreview>(this.url + "/generate", {prompt: prompt, generateImage: generateImage});
  }

  deleteMonster(monsterId: string): Observable<void> {
    return this.http.delete<void>(this.url + "/" + monsterId);
  }

  deleteImageFromMonster(monsterId: string, imageId: string): Observable<void> {
    return this.http.delete<void>(this.url + "/" + monsterId + "/image/" + imageId);
  }
}
