import { Component, OnInit } from '@angular/core';
import { Validators } from '@angular/forms';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'app/service/auth.service';
import { TokenStorageService } from 'app/service/token-storage.service';

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent implements OnInit {



  userForm: FormGroup;

  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';



  constructor( private authService: AuthService,
    public formBuilder: FormBuilder,
    private router: Router
    ) { }



  ngOnInit() {

    this.userForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(4)]],
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{13,}')
         ]
      ]
    })

  }

  get getControl(){
    return this.userForm.controls;
  }

  onSubmit(): void {
    if(this.userForm.valid){
      const username =  this.userForm.get("username").value
      const email =  this.userForm.get("email").value
      const password =  this.userForm.get("password").value


      this.authService.register(username, email, password).subscribe(
        data => {

          this.isSuccessful = true;
          this.router.navigate(['/login']);
          this.isSignUpFailed = false;
        },
        err => {
          this.errorMessage = err.error.message;
          this.isSignUpFailed = true;
        }
      );
    }
  }

  login(): void {
    this.router.navigate(['login']);
  }

  reloadPage(): void {
    window.location.reload();
  }

}
