import { Component, OnInit } from '@angular/core';
import {GetEmailsService} from "../services/get-emails.service";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit{
  uuid = "";
  results: any;

  constructor(
    private emails: GetEmailsService
  ){}
  ngOnInit(){
    this.uuid = window.location.href.split('uuid=')[1].split('&')[0];
    this.getEmails("inbox");
  }

  getEmails(folder: string){
    this.emails.getEmails(this.uuid, folder).forEach((data: any) => {
      this.results = data;
    });
  }
}