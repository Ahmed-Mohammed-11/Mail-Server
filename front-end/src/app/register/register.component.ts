import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {RegisterService} from "../services/register.service";
const shajs = require('sha.js');

// 7a8da028-af7f-4175-b063-e659a42d554e

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
  export class RegisterComponent implements OnInit{
  @Output() nameEmitterr = new EventEmitter < string > ();
  error1 :string = '';
  regpage:boolean = true;
  constructor(
      private rou :ActivatedRoute,private router: Router,
      private registerser : RegisterService
  ) {
  }
  ngOnInit() {
  }
  login(){
    this.nameEmitterr.emit("login");
  }

  log() {
    document.getElementsByClassName("error_message")[0].innerHTML = this.error1 ;
    let firstname = (document.getElementById('first') as HTMLInputElement | null)?.value;
    let secondname = (document.getElementById('second') as HTMLInputElement | null)?.value;
    let email = (document.getElementById('email') as HTMLInputElement | null)?.value;
    let aemail = (document.getElementById('aemail') as HTMLInputElement | null)?.value;
    let pass = (document.getElementById('pass') as HTMLInputElement | null)?.value;
    let cpass = (document.getElementById('cpass') as HTMLInputElement | null)?.value;
    let cap = (document.getElementById('cap') as HTMLInputElement | null)?.value;
    if (firstname===null||firstname===''){
      this.error1 = "please enter First Name";
    }
    else if(secondname===null||secondname===''){
      this.error1 = "please enter Second Name";
    }
    else if (email===null||email===''){
      this.error1 = "please enter an email";
    }
    else if (pass===null||pass===''){
      this.error1 = "please enter a password";
    }
    else if(cpass===null||cpass===''){
      this.error1 = "please confirm password";
    }
    else if (cap===null||cap===''){
      this.error1 = "please enter the captcha";
    }
    else if(pass!=cpass){
      this.error1 = "please enter a correct password";
    }
    else if(cap?.toLowerCase() !="embabytech"){
      this.error1 = "Captcha is incorrect";
    }
    else {
      pass = shajs('sha256').update(pass).digest('hex');
      // @ts-ignore
      email = email?.concat(aemail.toString());
      let pdata = {
        "firstname" :firstname,
        "lastname":secondname,
        "email":email,
        "password":pass,
      };
      this.registerser.register(pdata).subscribe(data=>{
        if(data!="this email is already taken") {
          this.router.navigate(["/login"]);
          this.nameEmitterr.emit("login");
          // @ts-ignore
          document.getElementById("register").style.display = "none";
          localStorage.setItem('email' , String(email));
        }
        else{
          this.error1 = "email is already used"
        }
      },error => {
        console.log(error);
      });
      let state ="available";

    }
    document.getElementsByClassName("error_message")[0].innerHTML = this.error1 ;
    console.log(this.error1);
  }
}
