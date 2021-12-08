import { Component, OnInit, ViewChild } from '@angular/core';
import { ImageModel } from 'app/models/imageModel';
import { ImageService } from 'app/service/image.service';
import { HttpErrorResponse } from '@angular/common/http';
import * as _ from 'lodash';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { AppDateAdapter, APP_DATE_FORMATS } from '../../shared/date-picker';
import { MatInput } from '@angular/material/input';
import { TokenStorageService } from 'app/service/token-storage.service';
@Component({
  selector: 'app-all-images',
  templateUrl: './all-images.component.html',
  styleUrls: ['./all-images.component.css'],
  providers: [
    { provide: DateAdapter, useClass: AppDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: APP_DATE_FORMATS },
  ],
})
export class AllImagesComponent implements OnInit {
  @ViewChild('dateinput', { read: MatInput }) dateinput: MatInput;
  constructor(private imageService: ImageService,
    private tokenStorage: TokenStorageService) {
    const currentYear = new Date().getFullYear();
    this.minDate = new Date(currentYear - 20, 0, 1);
  }
  images: ImageModel[] = [];
  fileteredImages: ImageModel[] = [];
  selectedDate: Date;
  searchFilter: ImageModel[];
  minDate: Date;
  error: any;
  selected: number;
  totalLength: any;
  page: number = 1;

  maxDate = new Date(new Date().getTime() + 86400000)
    .toISOString()
    .substring(0, 10);


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
    this.isDateSelected = false;
  }



  isDateSelected: boolean = false;


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


  public deleteImage(id: number) {
    this.selected = id;

    this.imageService.deleteImage(id).subscribe(
      (response: boolean) => {
        if (response) {
          this.searchFilter = this.fileteredImages;
          this.fileteredImages = [];
          this.fileteredImages = this.searchFilter.filter((x) => x.id != this.selected);
          let temp = this.images;
          this.images = temp.filter(x => x.id != this.selected);
        }
      },
      (error: HttpErrorResponse) => {
        this.error = error;
      }
    );
  }
  reset() {
    this.isDateSelected = false;
    this.selectedDate = null;
    this.dateinput.value = '';
    this.ngOnInit();
  }
}
