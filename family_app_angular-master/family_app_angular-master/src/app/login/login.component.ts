import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'app/service/auth.service';
import { TokenStorageService } from 'app/service/token-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: any = {
    username: null,
    password: null
  };

  errorMessage = '';
  isLoggedIn = false;
  isLoginFailed = false;
  roles: string[] = [];


  constructor(private authService: AuthService,
    private router: Router,
    private tokenStorage: TokenStorageService) { }

  ngOnInit() {

    if (this.tokenStorage.getToken()) {

      this.isLoggedIn = true;
      this.router.navigate(['/main/showImages']);
    }

  }

  onSubmit(): void {
    const { username, password } = this.form;

    this.authService.login(username, password).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);
        this.isLoginFailed = false;
        this.isLoggedIn = true;

        this.router.navigate(['/main/showImages']);


      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  onNew(): void {
    this.router.navigate(['signup']);
  }

  reloadPage(): void {
    window.location.reload();
  }

}
