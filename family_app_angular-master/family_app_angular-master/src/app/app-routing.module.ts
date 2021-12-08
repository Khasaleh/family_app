import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './sidebar/dashboard.component';
import { LoginComponent } from './login/login.component';

import { ShowImagesComponent } from './manage-uploaded/image-details-view/show-images.component';
import { AllImagesComponent } from './manage-uploaded/list-view-images/all-images.component';
import { RegisterUserComponent } from './register-user/register-user.component';

import { UploadImageComponent } from './upload/upload-image.component';
import { LoginLayoutComponent } from './login-layout/login-layout.component';
import { AuthGuard } from './service/auth.guard';
import { ManageUsersComponent } from './manage-users/manage-users.component';
import { UpdateUiComponent } from './manage-users/update-ui/update-ui.component';
import { AdminSignupComponent } from './admin-signup/admin-signup.component';


const routes: Routes = [



  { path: 'signup', component: RegisterUserComponent, data: { title: 'SIgnUp Component' } },
  { path: '', redirectTo: "login", data: { title: 'Login Component' }, pathMatch: "full" },
  {
    path: 'login', component: LoginLayoutComponent, data: { title: 'LoginLayout Component' },
    children: [
      { path: '', component: LoginComponent },

    ]


  },
  {
    path: 'main', component: DashboardComponent,
    children: [
      { path: 'upload', component: UploadImageComponent },
      { path: 'showImages', component: ShowImagesComponent },
      { path: 'allImages', component: AllImagesComponent },
      { path: 'users', component: ManageUsersComponent, data: { title: 'Users Component' } },
      { path: 'admin/signup', component: AdminSignupComponent, data: { title: 'AdminSignUp Component' } },
      { path: 'updates/:id', component: UpdateUiComponent, data: { title: 'Update Component' } },
    ]


  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
