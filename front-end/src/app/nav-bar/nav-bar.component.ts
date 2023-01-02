import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {FolderService} from "../services/folder.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit{
  page = "compose";
  empage = false;
  constructor(
      private rouv :ActivatedRoute,
      private myfolder : FolderService
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
    this.page= "compose";
    console.log(this.page);
  }
  public inbox(){
    this.page = "inbox";
    console.log(this.page);
  }
  public draft(){
    this.page= "draft";
    console.log(this.page);
  }
  public sent (){
    this.page = "sent"
    console.log(this.page);
  }
  public trash(){
    this.page= "trash";
    console.log(this.page);
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
        let from = localStorage.getItem('email');
        this.myfolder.create(name,String(from)).subscribe(data=>{
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

  cancelClicked() {
    this.isHidden = true;
    console.log(this.isHidden);
  }

}
