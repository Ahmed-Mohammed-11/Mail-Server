import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {FolderService} from "../services/folder.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit{
  page = "compose";
  empage = false;
  uuid = "";
  
  constructor(
      private rouv :ActivatedRoute,
      private router: Router,
      private myfolder : FolderService
  ) {
  }
  ngOnInit() {
    this.rouv.queryParams.subscribe((params:any)=>{
      if (window.location.href.includes('uuid=')){
        this.uuid = window.location.href.split('uuid=')[1].split('&')[0];
      }
      if (this.uuid.length != 36){
        // HIDE NAVBAR
        // @ts-ignore
        document.getElementById("nav").style.display = "none";
        this.router.navigate(["/login"]);
        return;
      }

      // SHOW NAVBAR
      // @ts-ignore
      document.getElementById("nav").style.display = "block";
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
