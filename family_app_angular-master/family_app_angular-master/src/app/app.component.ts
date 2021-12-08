import { Component, OnInit,ViewChild } from '@angular/core';
import { BreakpointObserver } from '@angular/cdk/layout';
import { MatSidenav } from '@angular/material/sidenav';
import * as _ from 'lodash';
import { TokenStorageService } from './service/token-storage.service';
import { AuthService } from './service/auth.service';
import { User } from './models/user';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent{

  isLoggedIn = false;
  username?: string;



  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;

  constructor( private tokenStorageService: TokenStorageService,
    public authService: AuthService,
    private observer: BreakpointObserver) {}


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



}
