import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private baseURL: string = "http://localhost:8081/";
  constructor(private http: HttpClient) { }
  public login(pdata:object):Observable<any> {
    const headers = { 'content-type': 'application/json' }
    console.log("create")
    return this.http.post(this.baseURL + 'login', JSON.stringify(pdata), { 'headers': headers ,responseType: 'json'});
  }
}
