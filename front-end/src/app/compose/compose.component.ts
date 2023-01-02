import { Component } from '@angular/core';
import {ComposeService} from "../services/compose.service";

@Component({
  selector: 'app-compose',
  templateUrl: './compose.component.html',
  styleUrls: ['./compose.component.css']
})
export class ComposeComponent {
  EmailId = 1;
  constructor(
      private serve: ComposeService
  ) {
  }
  ngOnInit() {
  }
  public send(){
    let to = (document.getElementById('to') as HTMLInputElement | null)?.value;
    let sub = (document.getElementById('subject') as HTMLInputElement | null)?.value;
    let message = (document.getElementById('message') as HTMLInputElement | null)?.value;
    let attachment = (document.getElementById('attachment') as HTMLInputElement | null)?.value;
    let from = localStorage.getItem('email');
    let uuid = window.location.href.split('uuid=')[1].split('&')[0];
    let mail = {
      "from":from,
      "emailID":this.EmailId.toString(),
      "to":to,
      "messageBody": message,
      "uuid": uuid
    }
    ++this.EmailId;
    console.log(mail);
    this.serve.compose(mail).subscribe(data=>{
      console.log(data)
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
  }
}
