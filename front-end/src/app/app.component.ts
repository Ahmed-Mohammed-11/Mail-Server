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

  constructor(private router: Router) { }

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
