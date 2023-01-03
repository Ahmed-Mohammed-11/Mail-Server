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
    console.log("getEmails")
    let response = this.http.get(this.baseURL + 'getEmails/' + uuid + '/' + folder);
    console.log(response.subscribe());
    return response;
  }
}
