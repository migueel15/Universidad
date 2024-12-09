import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SubirImagenesService {

  constructor() { }
  private url: string = '';

  setUrl(url: string) {
    this.url = url;
  }

  getUrl(): string {
    return this.url;
  }

  cloudinary.config{
    cloud_name=process.env["CLOUDINARY_CLOUD_NAME"]
    api_key=process.env["CLOUDINARY_API_KEY"]
    api_secret=process.env["CLOUDINARY_API_SECRET"]
    secure=True,
  }
}
