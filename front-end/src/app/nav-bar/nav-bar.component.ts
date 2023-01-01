import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit{
  empage = false;
  constructor(
      private rouv :ActivatedRoute
  ) {
  }
  ngOnInit() {
    this.rouv.queryParams.subscribe((params:any)=>{
      //console.log(params.data1);
      if (params.data1==="true"||params.data2==="true"){
        // @ts-ignore
        document.getElementById("nav").style.display = "block";
      }
      else {
        // @ts-ignore
        document.getElementById("nav").style.display = "none";
      }
    });
  }
  public compose(){
    console.log("compose")
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

  cancelClicked() {
    this.isHidden = true;
    console.log(this.isHidden);
  }

}
