import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api/citatelia/message'; // URL Spring Boot API

  constructor(private http: HttpClient) {}

  // Metóda na získanie správy zo Spring Boot
  getMessage(): Observable<string> {
    return this.http.get<string>(this.apiUrl);
  }
}
