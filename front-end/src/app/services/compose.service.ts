import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpParams, HttpRequest, HttpEvent} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ComposeService {
  private baseURL: string = "http://localhost:8081/";
  constructor(private http: HttpClient) { }
  public compose(pdata:object):Observable<any> {
    const headers = { 'content-type': 'application/json' }
    return this.http.post(this.baseURL + 'compose', JSON.stringify(pdata), { 'headers': headers ,responseType: 'text'});
  }
  
    // file from event.target.files[0]
    uploadFile(file: File): Observable<HttpEvent<any>> {
      let formData = new FormData();
      formData.append('file', file);
  
      let params = new HttpParams();
  
      const options = {
        params: params,
        reportProgress: true,
      };
      const req = new HttpRequest('POST', "http://localhost:8081/upload", formData, options);
      return this.http.request(req);
  }
}