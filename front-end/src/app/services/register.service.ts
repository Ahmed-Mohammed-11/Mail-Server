import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private baseURL: string = "http://localhost:8081/";
  constructor(private http: HttpClient) { }
  public register(pdata:object):Observable<any> {
    const headers = { 'content-type': 'application/json' }
    console.log("create")
    return this.http.post(this.baseURL + 'register', JSON.stringify(pdata), { 'headers': headers ,responseType: 'text'});
  }
}
