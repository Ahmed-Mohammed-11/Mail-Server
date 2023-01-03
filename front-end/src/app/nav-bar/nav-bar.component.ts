import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FolderService } from "../services/folder.service";
import { GetEmailsService } from "../services/get-emails.service";
import { ComposeService } from "../services/compose.service";
import {ListService} from "../services/list.service";

// Returns a random integer number less than max
function getRandomInt(max: number) {
  return Math.floor(Math.random() * max);
}

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit{
  page = "mails";     // Viewing page
  uuid = "";          // User ID
  display = "";       // Displayed Message (for success messages)
  folders: any;       // List of folders names
  results: any;       // List of emails
  full_results: any;  // Another copy of list of emails (Because results can be modified by search algorithms)
  email_preview: any; // Email Object for Preview

  
  constructor(
      private rouv: ActivatedRoute,
      private router: Router,
      private myfolder: FolderService,
      private emails: GetEmailsService,
      private serve: ComposeService,
      private emailfolder :ListService,
  ) {
  }
  ngOnInit() {
    this.rouv.queryParams.subscribe((params:any)=>{
      // Check if user is logged in
      if (window.location.href.includes('uuid=')){
        this.uuid = window.location.href.split('uuid=')[1].split('&')[0];
      }
      if (this.uuid.length != 36 && !window.location.href.includes('register')){
        // @ts-ignore
        return;
      }

      if (window.location.href.includes('register')){
        // SHOW REGISTER PAGE
      } else {
        // SHOW NAVBAR
        // @ts-ignore
        this.getFolders();
        this.getEmails("inbox");
        this.email_preview = this.results[0];
      }
    });
  }

  public setCurrentPage(page: string){
    if (page != this.page){
      this.page = page;
      this.getEmails(this.page);
      console.log('New Page View: ' + this.page);
    }
  }

  isHidden:boolean = true;
  buttonTitle:string = "";

  createClicked() {
    this.isHidden = false;
    console.log(this.isHidden);
    this.buttonTitle = "Create";
  } 

  deleteClicked() {
    this.isHidden = false;
    console.log(this.isHidden);
    this.buttonTitle = "Delete";
  }
  
  folder(){
    let name = (document.getElementById('folder-input') as HTMLInputElement | null)?.value;
    if (name != null&&name!='') {
      if (this.buttonTitle === "Create") {
        console.log(this.buttonTitle);
        this.myfolder.create(name, this.uuid).subscribe(data=>{
          console.log(data);
        },error => {
          console.log(error);
        });
      }
      else {
        console.log(this.buttonTitle);
      }
      this.isHidden = true;
    }
  }

  getFolders(){
    this.myfolder.getFolders(this.uuid).subscribe(data=>{
      this.folders = data;
      
    },error => {
      console.log(error);
    });
  }

  cancelClicked() {
    this.isHidden = true;
    console.log(this.isHidden);
  }

  search_for(){
    let search_key = (document.getElementById('search') as HTMLInputElement | null)?.value;
    this.results = [];
    for (let i = 0; i < this.full_results.length; i++) {
      if (this.full_results[i].subject?.toLowerCase().includes(search_key?.toLowerCase()) || this.full_results[i].from?.toLowerCase().includes(search_key?.toLowerCase()) || this.full_results[i]?.toLowerCase().to.includes(search_key?.toLowerCase())
      || this.full_results[i].messageBody?.toLowerCase().includes(search_key?.toLowerCase()) || this.full_results[i].datetime?.toLowerCase().includes(search_key?.toLowerCase())) {
        this.results.push(this.full_results[i]);
      }
    }
  }

  getEmails(folder: string){
    this.emails.getEmails(this.uuid, folder).forEach((data: any) => {
      this.full_results = data;
      this.results = this.full_results;
    });
  }

  public send(){
    let to = (document.getElementById('to') as HTMLInputElement | null)?.value;
    let sub = (document.getElementById('subject') as HTMLInputElement | null)?.value;
    let message = (document.getElementById('message') as HTMLInputElement | null)?.value;
    let attachment = (document.getElementById('attachment') as HTMLInputElement | null)?.value;
    let from = localStorage.getItem('email');
    let uuid = window.location.href.split('uuid=')[1].split('&')[0];
    let mail = {
      'emailID': getRandomInt(100000).toString(),
      "datetime": new Date().toLocaleString(),
      "from": from,
      "to": to,
      "messageBody": message,
      "uuid": uuid,
      "subject": sub,
      "attachment": attachment,
      "priority": "3"
    }
    console.log(mail);
    this.serve.compose(mail).subscribe(data=>{
      console.log(data)
      this.display = "Email sent successfully!";
      this.page = "inbox";
    },error => {
      console.log(error);
    });
    console.log(to,message,sub,attachment);
    console.log(localStorage.getItem('email'))
  }

  public draft(){
    let to = (document.getElementById('to') as HTMLInputElement | null)?.value;
    let sub = (document.getElementById('subject') as HTMLInputElement | null)?.value;
    let message = (document.getElementById('message') as HTMLInputElement | null)?.value;
    let attachment = (document.getElementById('attachment') as HTMLInputElement | null)?.value;
    let from = localStorage.getItem('email');
    console.log(to,message,sub,attachment);
    console.log(localStorage.getItem('email'))

    this.display = "The draft has been saved in `Drafts` folder.";
    this.page = "drafts";
  }

  sortEmails(){
    let select = (document.getElementById('sort') as HTMLInputElement | null)?.value;
    switch (select) {
      case "1":
        // Sort by priority
        // this.full_results = _.sortBy(this.full_results, 'first_nom');
        break;
      case "2":
        // Sort by Date
        break;

      case "3":
        // Sort by Sender
        break;

    }
  }

}