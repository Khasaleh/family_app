import { Component, ElementRef, OnInit } from '@angular/core';
import { HttpEventType, HttpResponse, HttpEvent } from '@angular/common/http';
import { AbstractControl, FormControl, FormGroupDirective, FormBuilder, FormGroup, NgForm, Validators } from "@angular/forms";
import { registerLocaleData } from '@angular/common';
import localeES from "@angular/common/locales/es";
registerLocaleData(localeES, "es");
import { formatDate } from "@angular/common";
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { ImageService } from '../service/image.service';
import { environment } from 'environments/environment';

import { HttpErrorResponse } from '@angular/common/http';
import { ImageModel } from '../models/imageModel';
import { Observable } from 'rxjs/internal/Observable';
import { ViewChild } from '@angular/core';
import { TokenStorageService } from 'app/service/token-storage.service';




@Component({
  selector: 'app-upload-image',
  templateUrl: './upload-image.component.html',
  styleUrls: ['./upload-image.component.css']
})
export class UploadImageComponent implements OnInit {


  constructor(public tokenStorageService: TokenStorageService,
    private imageService: ImageService,
    private formBuilder: FormBuilder,
    private router: Router) { }


  @ViewChild('inputFile') myInputVariable: ElementRef;

  images: ImageModel[] = [];
  categories: any[] = [];
  sources: any[] = [];
  catid: any;
  categoryId: any;


  imageUrl: string = "/assets/img/upload.png";
  imageFiles: File[];
  form: FormGroup;
  submitted = false;
  result: string = '';

  image: ImageModel = new ImageModel();
  httpClient: any;

  format = 'dd/MM/yyyy HH:mm:ss';
  myDate = Date.now();
  locale = 'en-US';
  formattedDate = formatDate(this.myDate, this.format, this.locale);
  filenames: string[] = [];

  selectedFiles?: FileList;
  progressInfos: { value: number, fileName: string, msg: string; }[] = [];
  messages: string[] = [];







  ngOnInit() {

    this.form = this.formBuilder.group({

      categoryresults: ['', Validators.required],
      sourceresults: ['', Validators.required]

    });
  }

  get f(): { [key: string]: AbstractControl; } {
    return this.form.controls;
  }


  onFileChange(event: any): void {
    this.imageFiles = event.target.files;
    var selectFile = event.target.files;
    for (var i = 0; i < selectFile.length; i++) {
    }
  }



  uploadFiles(): void {
    this.messages = [];

    if (this.imageFiles) {
      for (let i = 0; i < this.imageFiles.length; i++) {
        this.upload(i, this.imageFiles[i]);
      }
    }
  }

  upload(idx: number, file: File): void {
    this.progressInfos[idx] = { value: 0, fileName: file.name, msg: "" };

    if (file) {
      this.imageService.uploadImages(this.form.value.categoryresults, this.form.value.sourceresults, this.tokenStorageService.getUser().id, new Date(Date.now()), file).subscribe(
        (event: any) => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progressInfos[idx].value = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            const msg = 'Uploaded the file successfully: ';
            this.progressInfos[idx].msg = msg;

          }
        },
        (err: any) => {
          this.progressInfos[idx].value = 0;
          const msg = 'Could not upload the file: ' + err.error;
          this.progressInfos[idx].msg = msg;
        });
    }
  }


  cancelNow() {

    this.myInputVariable.nativeElement.value = '';
    this.progressInfos.length = 0;
    this.messages.length = 0;
    this.images.length = 0;
    this.form.reset();


  }
}
