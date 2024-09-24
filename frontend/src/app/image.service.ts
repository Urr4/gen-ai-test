import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {ImageWrapper} from "./models/image/image-wrapper";

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  url: string = "http://localhost:8080/images"

  constructor(private http: HttpClient) {
  }

  getImageById(uuid: string): Observable<ImageWrapper> {
    return this.http.get<Blob>(this.url + "/" + uuid, {responseType: 'blob' as 'json', observe: 'response'})
      .pipe(
        map(response => {
          const imageId: string = response.headers.get("image-id")!;
          const image: string = URL.createObjectURL(response.body!)
          return new ImageWrapper(imageId, image);
        })
      );
  }
}
