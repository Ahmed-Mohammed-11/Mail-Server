import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'mail-server';
  pagec = "login";
  uuid = "";

  constructor(private router: Router) {
    if (window.location.href.includes('uuid=')){
      this.uuid = window.location.href.split('uuid=')[1].split('&')[0];
    }
    if (this.uuid.length != 36 && !window.location.href.includes('register')){
      this.pagec = "login";
    } else {
      this.pagec = "nav";
    }
  }

  goToPage(pageName: string): void {
    this.router.navigate([`${pageName}`]);
  }
  receive_from_reg($event: string) {
    this.pagec = $event;
  }
  receive_from_log($event: string) {
    this.pagec = $event;
  }
}
