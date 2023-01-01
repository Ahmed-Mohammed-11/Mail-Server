import { Component } from '@angular/core';

@Component({
  selector: 'app-compose',
  templateUrl: './compose.component.html',
  styleUrls: ['./compose.component.css']
})
export class ComposeComponent {

  ngOnInit() {

  }
  public send(){
    let to = (document.getElementById('to') as HTMLInputElement | null)?.value;
    let sub = (document.getElementById('subject') as HTMLInputElement | null)?.value;
    let message = (document.getElementById('message') as HTMLInputElement | null)?.value;
    let attachment = (document.getElementById('attachment') as HTMLInputElement | null)?.value;
    console.log(to,message,sub,attachment);
  }
  public draft(){
    let to = (document.getElementById('to') as HTMLInputElement | null)?.value;
    let sub = (document.getElementById('subject') as HTMLInputElement | null)?.value;
    let message = (document.getElementById('message') as HTMLInputElement | null)?.value;
    let attachment = (document.getElementById('attachment') as HTMLInputElement | null)?.value;
    console.log(to,message,sub,attachment);
  }
}
