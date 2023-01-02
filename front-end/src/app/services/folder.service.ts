import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import * as path from "path";

@Injectable({
  providedIn: 'root'
})
export class FolderService {
  private baseURL: string = "http://localhost:8081/";
  constructor(private http: HttpClient) { }
  public create(name:string,user:string):Observable<any> {
    const headers = { 'content-type': 'application/json' }
    console.log("create")
    return this.http.post(this.baseURL + "createFolder/"+name, user,{ 'headers': headers ,responseType: 'text'});
  }
}
