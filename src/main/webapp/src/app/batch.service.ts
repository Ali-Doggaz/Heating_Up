import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

const httpOptionsPlain = {
  headers: new HttpHeaders({
    'Accept': 'text/plain',
    'Content-Type': 'text/plain'
  }),
  'responseType': 'text'
};

@Injectable({providedIn: 'root'})
export class BatchService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient){}

  public startScan(): Observable<String> {
    return this.http.get(`${this.apiServerUrl}/Scan/Start`, {responseType: 'text'});
  }

  public stopScan(): Observable<String> {
    return this.http.get<String>(`${this.apiServerUrl}/Scan/Stop`);
  }

}
