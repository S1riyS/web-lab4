import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs';

interface Token {
  token: string,
  expirationDate: number
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) { }

  auth(username: string, token: string): void {
    this.username = username;
    this.token = token;
  }

  isLoggedIn(): boolean {
    return !!this.token;
  }

  logOut(): void {
    this.username = "";
    this.token = "";
  }

  login (username: string, password: string): Observable<string> {
    this.logOut();
    return this.http.post<Token>(
      `${environment.apiUrl}${environment.loginPath}`, 
      { username, password }
    ).pipe(
      map(
        (result) => {
          this.auth(username, result.token);
          return result.token;
        }
      )
    );
  }

  register (username: string, password: string): Observable<string> {
    this.logOut(); 
    return this.http.post<Token>(
      `${environment.apiUrl}${environment.registerPath}`, 
      { username,  "email": "user@example.com", password }
    ).pipe(
      map(
        (result) => {
          this.auth(username, result.token);
          return result.token;
        }
      )
    );
  }

  set token(value: string) {
    localStorage.setItem("api-token", value);
  }

  get token(): string {
    return localStorage.getItem("api-token") || "";
  }

  set username(value: string) {
    localStorage.setItem("username", value);
  }

  get username(): string {
    return localStorage.getItem("username") || "";
  }
}
