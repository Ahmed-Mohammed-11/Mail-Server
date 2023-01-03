import { Injectable } from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ListService {
  private _EmailFolder = new Subject<string>();
  EmailFolder$ = this._EmailFolder.asObservable();
  constructor() { }
  send_folder_name(name:string){
    this._EmailFolder.next(name);
  }
}
