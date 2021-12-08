import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'app/models/user';
import { ImageService } from 'app/service/image.service';
import { Observable } from 'rxjs';
import { BrowserModule } from '@angular/platform-browser';


@Component({
  selector: 'app-manage-users',
  templateUrl: './manage-users.component.html',
  styleUrls: ['./manage-users.component.css']
})
export class ManageUsersComponent implements OnInit {

  users: User[]

  constructor(private userService: ImageService,
    private router: Router) { }

  ngOnInit() {

    this.reloadData();
  }

  reloadData() {
     this.userService.getUsersList().subscribe(users=>{this.users=users
    });
  }

  updateUser(id: number){
    this.router.navigate(['main/updates', id]);
  }

  deleteUser(id: number) {
    this.userService.deleteUser(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  onCreate(): void{
    this.router.navigate(['main/admin/signup']);
  }

   onEnabled(id: number, enabled:boolean): void{

       if(enabled){
         this.userService.enabledUser(id, false).subscribe(data  => {
           console.log(data);
         },
         error => console.log(error));
       }
       else{
         this.userService.enabledUser(id, true).subscribe(data  => {
          console.log(data);
        },
        error => console.log(error));

       }
       location.reload();

   }

}
