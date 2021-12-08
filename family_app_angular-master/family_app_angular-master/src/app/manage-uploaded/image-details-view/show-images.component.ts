import { Component, OnInit, ViewChild } from '@angular/core';
import * as _ from 'lodash';
import { ImageModel } from 'app/models/imageModel';
import { ImageService } from '../../service/image.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { AppDateAdapter, APP_DATE_FORMATS } from '../../shared/date-picker';
import { MatInput } from '@angular/material/input';
import { TokenStorageService } from 'app/service/token-storage.service';


@Component({
  selector: 'app-show-images',
  templateUrl: './show-images.component.html',
  styleUrls: ['./show-images.component.css'],

  providers: [
    { provide: DateAdapter, useClass: AppDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: APP_DATE_FORMATS },
  ],
})
export class ShowImagesComponent implements OnInit {

  images: ImageModel[] = [];
  categories: any[] = [];
  fileteredImages: ImageModel[] = [];
  error: any;
  selectedDate: Date;
  searchFilter: ImageModel[];
  minDate: Date;
  selected: number;
  totalLength: any;
  page: number = 1;
  allchecked: boolean;
  userId: number = 1;

  maxDate = new Date(new Date().getTime() + 86400000)
    .toISOString()
    .substring(0, 10);
  @ViewChild('dateinput', { read: MatInput }) dateinput: MatInput;
  isDateSelected: boolean;

  constructor(private imageService: ImageService,
    private tokenStorage: TokenStorageService,
    private router: Router) {
    const currentYear = new Date().getFullYear();
    this.minDate = new Date(currentYear - 20, 0, 1);
  }



  admin: Boolean = false;


  ngOnInit() {

    this.tokenStorage.getUser().roles.forEach((role) => {
      if (role == 'ROLE_ADMIN') {
        this.admin = true;
      }
    });
    if (this.admin) {
      this.getImages();
      return;
    } else {
      this.getImagesById();
    }

    this.allchecked = false;
    this.isDateSelected = false;
  }



  selectDate(event: MatDatepickerInputEvent<Date>) {
    this.selectedDate = event.value;
    this.isDateSelected = true;
  }

  public getImages(): void {
    this.imageService.getImages().subscribe((response: ImageModel[]) => {
      this.images = response;
      this.fileteredImages = this.images;

    });

    (error: HttpErrorResponse) => {
      this.error = error;
    };


  }
  public getImagesById(): void {
    this.imageService.getImagesByUser(this.tokenStorage.getUser().id).subscribe((response: ImageModel[]) => {
      this.images = response;
      this.fileteredImages = this.images;

      (error: HttpErrorResponse) => {
        this.error = error;
      };


    });
  }


  checkAllCheckBox(ev: any) {
    this.fileteredImages.forEach(x => x.checked = ev.checked);
  }

  isAllCheckBoxChecked() {
    return this.fileteredImages.every(p => p.checked);
  }

  public deleteImage() {
    const selectedImages = this.fileteredImages.filter(image => image.checked).map(p => p.id);

    selectedImages.forEach(id => {
      this.imageService.deleteImage(id).subscribe(
        (response: boolean) => {
          if (response) {
            this.searchFilter = this.fileteredImages;
            this.fileteredImages = [];
            this.fileteredImages = this.searchFilter.filter((x) => x.id != id);
            let temp = this.images;
            this.images = temp.filter(x => x.id != this.selected);
          }
        },
        (error: HttpErrorResponse) => {
          this.error = error;
        }
      );
    });
    this.allchecked = false;
  }

  reset() {
    this.isDateSelected = false;
    this.selectedDate = null;
    this.dateinput.value = '';
    this.ngOnInit();
  }

}
