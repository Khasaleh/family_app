import { Component, OnInit, ViewChild } from '@angular/core';
import { BreakpointObserver } from '@angular/cdk/layout';
import { MatSidenav } from '@angular/material/sidenav';
import { TokenStorageService } from 'app/service/token-storage.service';
import { Router } from '@angular/router';
import { User } from 'app/models/user';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {


  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;


  constructor( public tokenStorageService: TokenStorageService,
    private router: Router,
    private observer: BreakpointObserver) { }


 admin:boolean = false

  ngOnInit(  ) {

    this.tokenStorageService.getUser().roles.forEach((role) => {
      if (role === 'ROLE_ADMIN') {
        this.admin = true;
      }
    });
  }

  ngAfterViewInit() {

    this.observer.observe(['(max-width: 800px)']).subscribe((res) => {

      if (res.matches) {

        this.sidenav.mode = 'over';
        this.sidenav.close();
      } else {
        this.sidenav.mode = 'side';
        this.sidenav.open();

      }

    });

  }

  logout(): void {
    this.tokenStorageService.signOut();
    this.router.navigate(['login']);
  }


}
