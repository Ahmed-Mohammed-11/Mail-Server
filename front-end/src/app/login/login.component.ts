import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {LoginService} from "../services/login.service";
const shajs = require('sha.js');

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  error: string = '';
  ngOnInit() {}

  constructor(
      private router :Router,private rou:ActivatedRoute,
      private serve :LoginService
  ) {
  }

  register(){
    this.router.navigate(["/main/"],{queryParams:{data1:"false"}});
    // @ts-ignore
  }

  login() {
    document.getElementsByClassName("error_message1")[0].innerHTML = this.error;
    let email = (document.getElementById('emaill') as HTMLInputElement | null)?.value;
    let pass = (document.getElementById('passl') as HTMLInputElement | null)?.value;
    if ((email===null || email==='') || (pass===null||pass==='')) {
      this.error = "Email or Password cannot be empty";
    } else {
      pass = shajs('sha256').update(pass).digest('hex');
      let pdata = {
        "email":email,
        "password":pass,
      };
      this.serve.login(pdata).subscribe(data=>{
        console.log(data.status);
        if(data.status==="success") {
          this.router.navigate(["/main"], {queryParams: {uuid: data.uuid}});
          localStorage.setItem('email' , String(email));
        }
        else {
          this.error = "Invalid username or password";
        }
      },error => {
        console.log(error);
      });
      //console.log(pdata);
    }
    document.getElementsByClassName("error_message1")[0].innerHTML = this.error;
    console.log(this.error);

  }
}
