import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { MatGridListModule } from '@angular/material/grid-list';
import { DatePipe } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UploadImageComponent } from './upload/upload-image.component';
import { AngularMaterialModule } from './angular-material.module';
import { FooterComponent } from './footer/footer.component';
import { ShowImagesComponent } from './manage-uploaded/image-details-view/show-images.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ImageService } from '../app/service/image.service';
import { AllImagesComponent } from './manage-uploaded/list-view-images/all-images.component';
import { HttpClientModule } from '@angular/common/http';
import { MatChipsModule } from '@angular/material/chips';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { NgxPaginationModule } from 'ngx-pagination';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { RegisterUserComponent } from './register-user/register-user.component';
import { DashboardComponent } from './sidebar/dashboard.component';
import { authInterceptorProviders } from './service/auth.interceptor';
import { AuthService } from './service/auth.service';
import { TokenStorageService } from './service/token-storage.service';
import { LoginComponent } from './login/login.component';
import { LoginLayoutComponent } from './login-layout/login-layout.component';
import { MatMenuModule } from '@angular/material/menu';
import { ManageUsersComponent } from './manage-users/manage-users.component';
import { UpdateUiComponent } from './manage-users/update-ui/update-ui.component';
import { AdminSignupComponent } from './admin-signup/admin-signup.component';


@NgModule({
  declarations: [
    AppComponent,
    UploadImageComponent,
    FooterComponent,
    ShowImagesComponent,
    AllImagesComponent,
    RegisterUserComponent,
    DashboardComponent,
    LoginComponent,
    LoginLayoutComponent,
    ManageUsersComponent,
    UpdateUiComponent,
    AdminSignupComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    AngularMaterialModule,
    NgbModule,
    FontAwesomeModule,
    MatToolbarModule,
    MatDatepickerModule,
    MatSidenavModule,
    MatButtonModule,
    MatFormFieldModule,
    MatTabsModule,
    MatCardModule,
    MatIconModule,
    MatDividerModule,
    HttpClientModule,
    MatGridListModule,
    MatMenuModule,
    FlexLayoutModule,
    MatChipsModule,
    MatAutocompleteModule,
    NgxPaginationModule,
    MatCheckboxModule,

  ],
  providers: [ImageService, DatePipe, authInterceptorProviders, AuthService, TokenStorageService],
  bootstrap: [AppComponent],
})
export class AppModule { }
