import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
const shajs = require('sha.js');


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
  export class RegisterComponent implements OnInit{
  error1 :string = '';
  regpage:boolean = true;
  constructor(
      private rou :ActivatedRoute,private router: Router
  ) {
  }
  ngOnInit() {
    this.rou.queryParams.subscribe((params:any)=>{
      console.log(params);
      if (params.data1==="false"){
        //console.log("register");
        // @ts-ignore
        document.getElementById("register").style.display = "block";
      }
      else {
        // @ts-ignore
        document.getElementById("register").style.display = "none";
      }
    });
  }

  log() {
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
    else if(cap!="EmbabyTeCh"){
      this.error1 = "wrong captcha";
    }
    else {
      pass = shajs('sha256').update(pass).digest('hex');
      // @ts-ignore
      email = email?.concat(aemail.toString());
      let pdata = [firstname,secondname,email,pass];
      JSON.stringify(pdata);
      let state ="available";
      if(state==="available") {
        this.router.navigate(["/nav-bar/"], {queryParams: {data2: "true"}});
        // @ts-ignore
        document.getElementById("register").style.display = "none";
      }
      else{
        this.error1 = "email is already used"
      }
    }
    console.log(pass);
    console.log(email);
    console.log(this.error1);
  }
}
