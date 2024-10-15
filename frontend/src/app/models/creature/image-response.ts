export class ImageResponse {
  imageId: string;
  imageBytes: Blob;

  constructor(imageId: string, imageBytes: Blob) {
    this.imageId = imageId;
    this.imageBytes = imageBytes;
  }
}
