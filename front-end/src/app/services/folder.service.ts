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

  public create(name:string,uuid:string):Observable<any> {
    const headers = { 'content-type': 'application/json' }
    console.log("create")
    return this.http.post(this.baseURL + "createFolder/"+uuid+"/"+name, {},{ 'headers': headers ,responseType: 'text'});
  }
  
  public deleteFolder(name: string, uuid: string):Observable<any>{
    console.log('deleteFolder')
    const headers = { 'content-type': 'application/json' }
    return this.http.delete(this.baseURL + "deleteFolder/"+uuid+"/"+name, {'headers': headers, responseType: 'text'});
  }

  public getFolders(uuid: string):Observable<any>{
    console.log('getFolders')
    return this.http.get(this.baseURL + "getFolders/"+uuid);
  }
}
