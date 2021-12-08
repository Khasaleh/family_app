import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'app/models/user';
import { ImageService } from 'app/service/image.service';

@Component({
  selector: 'app-update-ui',
  templateUrl: './update-ui.component.html',
  styleUrls: ['./update-ui.component.css'],
})
export class UpdateUiComponent implements OnInit {
  userForm: FormGroup;
  id: number;
  user: User;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public formBuilder: FormBuilder,
    private userService: ImageService
  ) {}

  ngOnInit() {
    this.userForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(4)]],
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.pattern(/^$|((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{13,})/)
        ],
      ],
    });

    this.user = new User();

    this.id = this.route.snapshot.params['id'];

    this.userService.getUser(this.id).subscribe(
      (data) => {
        this.user = data;
      },
      (error) => console.log(error)
    );
  }

  get getControl() {
    return this.userForm.controls;
  }



  password: string = '';
  updateUser() {

      if (this.userForm.valid) {
        const username = this.userForm.get('username').value;
        const email = this.userForm.get('email').value;
        const password = this.userForm.get('password').value;

        this.userService
          .updateUsers(this.id, username, email, password)
          .subscribe(
            (data) => {
              this.user = new User();
              this.gotoList();
            },
            (error) => console.log(error)
          );
      }

  }

  onSubmit() {
    this.updateUser();
  }

  gotoList() {
    this.router.navigate(['main/users']);
  }
}
