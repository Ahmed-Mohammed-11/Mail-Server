import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FolderService } from "../services/folder.service";
import { GetEmailsService } from "../services/get-emails.service";
import { ComposeService } from "../services/compose.service";

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
  page = "inbox";     // Viewing page
  uuid = "";          // User ID
  display = "";       // Displayed Message (for success messages)
  folders: any;       // List of folders names
  results: any;       // List of emails
  full_results: any;  // Another copy of list of emails (Because results can be modified by search algorithms)
  email_preview: any; // Email Object for Preview
  cursor = 0;         // Cursor for navigating emails
  reply_to = "";      // Reply-To field
  reply_subject = "";
  isHidden:boolean = true;
  buttonTitle:string = "";
  pagenumber = 1;
  page_size = 10;
  compose_attachments:String[] = [];
  SPRINGBOOT = "http://localhost:8081/";
  CURRENT_FOLDER = "inbox";

  
  constructor(
      private rouv: ActivatedRoute,
      private myfolder: FolderService,
      private emails: GetEmailsService,
      private serve: ComposeService,
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
        this.refresh();
      }
    });
  }

  public refresh(){
    this.getFolders();
    this.getEmails(this.page);
    this.search_for();
    this.sortEmails();
  }

  isPriorityBadgeActive(x: number, y: String){
    var j: number = +y;
    if (x <= j){
      return "active";
    }
    return "";
  }

  public setCurrentPage(page: string, to=null, subject=null){
    if (page != this.page){
      this.page = page;
      if (page != 'preview' && page != 'compose'){
        this.CURRENT_FOLDER = page;
      }
      console.log(page);
      if (to != null && subject != null){
        this.reply_to = to;
        this.reply_subject = "Re: " + subject;
      }
      // this.refresh();
    }
  }

  public viewEmail(emailID: string){
    for (let i = 0; i < this.results.length; i++){
      if (this.results[i].emailID == emailID){
        this.email_preview = this.results[i];
        this.setCurrentPage('preview');
        this.cursor = i;
        break;
      }
    }
  }

  public navigateEmails(direction: string){
    console.log(this.results);
    if (direction == 'forward' && this.cursor != this.results.length - 1){
      this.cursor += 1;
    } else if (direction == "back" && this.cursor != 0) {
      this.cursor -= 1;
    }
    this.email_preview = this.results[this.cursor];
  }

  public pagination(n: number){
    if (n > 0 && this.pagenumber*this.page_size < this.results.length - 1){
      this.refresh();
      this.pagenumber += 1;
    } else if (n < 0 && this.pagenumber != 1) {
      this.refresh();
      this.pagenumber -= 1;
    }
  }

  createClicked() {
    this.isHidden = false;
    console.log(this.isHidden);
    this.buttonTitle = "Create";
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
      else if (this.buttonTitle === "Delete") {
        console.log(name);
        if (name != null) {
          this.myfolder.deleteFolder(name, this.uuid).subscribe(data=>{
            console.log(data);
          },error => {
            console.log(error);
          });
        }
      } 
      // else if (this.buttonTitle === "Move"){
        // this.moveEmail(this.temp_emailID, this.temp_oldFolder, name);
      // }
      this.isHidden = true;
    }
  }

  deleteFolder(){
    this.isHidden = false;
    this.buttonTitle = "Delete";
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
    try{
      if (this.full_results == null){return}
      let search_key = (document.getElementById('search') as HTMLInputElement | null)?.value;
      this.results = [];
      for (let i = 0; i < this.full_results.length; i++) {
        if (this.full_results[i].subject?.toLowerCase().includes(search_key?.toLowerCase()) || this.full_results[i].from?.toLowerCase().includes(search_key?.toLowerCase()) || this.full_results[i]?.toLowerCase().to.includes(search_key?.toLowerCase())
        || this.full_results[i].messageBody?.toLowerCase().includes(search_key?.toLowerCase()) || this.full_results[i].datetime?.toLowerCase().includes(search_key?.toLowerCase())) {
          this.results.push(this.full_results[i]);
        }
      }
    } catch (e) {
      console.log(e);
    }
  }

  // temp_emailID = "";
  // temp_oldFolder = "";
  moveEmail(emailID: string, oldFolder: string, newFolder: string){
    if (newFolder == ""){
      this.isHidden = false;
      this.buttonTitle = "Move";
      // this.temp_emailID = emailID;
      // this.temp_oldFolder = oldFolder;
      return;
    }
    this.emails.moveEmail(this.uuid, oldFolder, emailID, newFolder).subscribe(data=>{
      console.log(data);
    }, error => {
      console.log(error);
    });
  }

  // Handle Emails
  getEmails(folder: string){
    this.emails.getEmails(this.uuid, folder).forEach((data: any) => {
      this.full_results = data;
      this.results = this.full_results;
    });
  }

  deleteEmail (emailID: string){
    this.emails.deleteEmail(this.uuid, this.page, emailID).subscribe(data=>{
      console.log(data);
    },error => {
      console.log(error);
    });
  }

  public send(){
    let to = (document.getElementById('to') as HTMLInputElement | null)?.value;
    let sub = (document.getElementById('subject') as HTMLInputElement | null)?.value;
    let message = (document.getElementById('message') as HTMLInputElement | null)?.value;
    let from = localStorage.getItem('email');
    let uuid = window.location.href.split('uuid=')[1].split('&')[0];
    let mail = {
      'emailID': "mail_"+getRandomInt(100000).toString(),
      "datetime": new Date().toLocaleString(),
      "from": from,
      "to": [to],
      "messageBody": message,
      "uuid": uuid,
      "subject": sub,
      "attachment": this.compose_attachments.join(','),
      "priority": "0"
    }
    // Error Handling
    if ((to == null || sub == null || message == null || from == null || uuid == null) || (to == "" || sub == "" || message == "" || from == "" || uuid == "")) {
      this.display = "Please fill out all fields";
      return;
    }
    this.serve.compose(mail).subscribe(data=>{
      console.log(data);
      this.display = "Email sent successfully!";
      this.page = "inbox";
    },error => {
      console.log(error);
    });
    this.page = "inbox";
    this.refresh();
  }

  public setPriority(email: any, newPriority: number){
    // DELETE INITIAL EMAIL
    this.emails.deleteEmail(this.uuid, this.page, email.emailID).subscribe(data=>{
      console.log(data);
    },error => {
      console.log(error);
    });

    // CREATE NEW EMAIL
    let mail = {
      'emailID': email.emailID,
      "datetime": email.datetime,
      "from": email.from,
      "to": email.to.split(','),
      "messageBody": email.messageBody,
      "uuid": this.uuid,
      "subject": email.subject,
      "attachment": email.attachment,
      "priority": newPriority.toString()
    }

    this.serve.compose(mail).subscribe(data=>{
      console.log(data);
      this.page = "inbox";
    },error => {
      console.log(error);
    });
    // this.refresh();
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
    let sorted = [];
    // let 
    switch (select) {
      case "1":
        // Sort by Priority
        break;
      case "2":
        // Sort by Date
        break;

      case "3":
        // Sort by Sender
        break;

    }
  }

  fileChange(event: any): void{
    if (event){
      const fileList: FileList = event.target.files;
      if (fileList.length > 0) {
        const file = fileList[0];
        this.compose_attachments.push(file.name);
        this.serve.uploadFile(file).subscribe(data=>{
          console.log(data)
        },error => {
          console.log(error);
        });
        console.log('Uploaded!');
      }
    }
  }

  popAttachment(attachment: String){
    this.compose_attachments = this.compose_attachments.filter((item) => item !== attachment);
  }

}