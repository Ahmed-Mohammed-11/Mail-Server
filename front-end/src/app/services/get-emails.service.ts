import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class GetEmailsService {
  private baseURL: string = "http://localhost:8081/";
  
  constructor(private http: HttpClient) { }

  public getEmails(uuid: string, folder: string):Observable<any> {
    let response = this.http.get(this.baseURL + 'getEmails/' + uuid + '/' + folder);
    return response;
  }
  
  public deleteEmail(uuid: string, folder: string, emailid: string):Observable<any>{
    const headers = { 'content-type': 'application/json' }
    let response = this.http.delete(this.baseURL + 'deleteEmail/' + uuid + '/' + folder + '/' + emailid, {'headers': headers, responseType: 'json'});
    return response;
  }

  public moveEmail(uuid: string, folder: string, emailid: string, newFolder: string):Observable<any>{
    const headers = { 'content-type': 'application/json' }
    let response = this.http.post(this.baseURL + 'moveEmail/' + uuid + '/' + folder + '/' + newFolder + '/' + emailid, {}, {'headers': headers, responseType: 'json'});
    return response;
  }

}
