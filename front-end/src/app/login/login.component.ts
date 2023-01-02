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
  error : string = '';
  ngOnInit() {
    /*this.rou.queryParams.subscribe((params:any)=>{
      console.log(params.data2);
      if (params.data2==="false"){
        // @ts-ignore
        document.getElementById("log").style.display = "block";
      }
      else {
        // @ts-ignore
        document.getElementById("log").style.display = "none";
      }
    });*/
  }

  constructor(
      private router :Router,private rou:ActivatedRoute,
      private serve :LoginService
  ) {
  }

  register(){
    this.router.navigate(["/nav-bar/"],{queryParams:{data1:"false"}});
    // @ts-ignore
  }
  login() {
    document.getElementsByClassName("error_message1")[0].innerHTML = this.error;
    let email = (document.getElementById('emaill') as HTMLInputElement | null)?.value;
    let pass = (document.getElementById('passl') as HTMLInputElement | null)?.value;
    if (email===null||email===''){
      this.error = "please enter an email";
    }
    else if(pass===null||pass===''){
      this.error = "please enter a password";
    }
    else{
      pass = shajs('sha256').update(pass).digest('hex');
      let pdata ={
        "email":email,
        "password":pass,
      };
      this.serve.login(pdata).subscribe(data=>{
        console.log(data.status);
        if(data.status==="success") {
          this.router.navigate(["/nav-bar"], {queryParams: {data1: "true"}});
          localStorage.setItem('email' , String(email));
          // @ts-ignore
          // document.getElementById("log").style.display = "none";
        }
        else {
          this.error = "wrong username or password";
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
