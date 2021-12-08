import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environments/environment';
import { ImageModel } from '../models/imageModel';
import { DatePipe } from '@angular/common';
import { Observable } from 'rxjs';
import { User } from 'app/models/user';


@Injectable({
  providedIn: 'root',
})
export class ImageService {
  searchOption: any[];
  imagesData: ImageModel[];

  constructor(private http: HttpClient, public datepipe: DatePipe) { }



  private apiServerUrl = 'http://localhost:8002/api';
  private uploaddDeleteurl = environment.uploadDeleteUrl;
  private baseUrl = 'http://localhost:8002/api/auth/users';
  private updateUser = 'http://localhost:8002/api/auth/update';
  private updateUserEnabled = 'http://localhost:8002/api/auth/updateEnbaled';
  private deleteapi = 'http://localhost:8002/api/auth/delete';

  public uploadImages(catId: number, sourceId: number, userId: number, date: Date, files: File): Observable<HttpEvent<any>> {



    let formData: FormData = new FormData();

    formData.append('fileImage', files);
    formData.append('source', sourceId.toString());
    formData.append('category', catId.toString());
    formData.append('id', userId.toString());
    formData.append('date', date.toString());

    const req = new HttpRequest('POST', `${this.uploaddDeleteurl}/imageupload`, formData, {
      reportProgress: true,
      responseType: 'text' as 'json'
    });

    return this.http.request(req);

  }
  public getImages(): Observable<ImageModel[]> {
    return this.http.get<ImageModel[]>(`${this.apiServerUrl}/images`);
  }

  public getImagesByUser(userId: number): Observable<ImageModel[]> {
    return this.http.get<ImageModel[]>(`${this.apiServerUrl}/images/${userId}`);
  }


  public deleteImage(id: any) {
    return this.http.delete<boolean>(
      `${this.uploaddDeleteurl}/images/delete/${id}`
    );
  }



  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }

  getUsersList(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}`);
  }

  updateUsers(id: number, username: string, email: string, password: string): Observable<Object> {

    let formData: FormData = new FormData();

    formData.append('email', email);
    formData.append('username', username);
    formData.append('password', password);
    return this.http.put(`${this.updateUser}/${id}`, formData);
  }

  enabledUser(id: number, enabled: boolean): Observable<Object> {

    let formData: FormData = new FormData();

    formData.append('enabled', enabled.toString());

    return this.http.put(`${this.updateUserEnabled}/${id}`, formData);
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.deleteapi}/${id}`, { responseType: 'text' });
  }





}
