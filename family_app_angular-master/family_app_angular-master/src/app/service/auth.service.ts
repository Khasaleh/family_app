import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';


const AUTH_API = 'http://localhost:8002/api/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'content-type': 'application/json' })
};


@Injectable()
export class AuthService {

  isAuthenticated = false;

constructor(private http: HttpClient) { }


login(username: string, password: string): Observable<any> {
  this.isAuthenticated = true;
  return this.http.post(AUTH_API + 'signin', {
    username,
    password

  }, httpOptions);
  this.isAuthenticated = true;

}

register(username: string, email: string, password: string): Observable<any> {
  return this.http.post(AUTH_API + 'signup', {
    username,
    email,
    password
  }, httpOptions);
}

}
