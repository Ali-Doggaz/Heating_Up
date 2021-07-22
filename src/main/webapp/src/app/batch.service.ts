import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import {batchConfig} from "./batchConfig";

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
    return this.http.get(`${this.apiServerUrl}/Scan/Stop`, {responseType: 'text'});
  }

  public addConfig(newConfig: batchConfig): Observable<String> {
    return this.http.post(`${this.apiServerUrl}/Scan/SetConfig`, newConfig, {responseType: 'text'});
  }

  public getConfigs(): Observable<batchConfig[]>{
    return this.http.get<batchConfig[]>(`${this.apiServerUrl}/Scan/getConfigs`);
  }

  public deleteConfig(id: number): Observable<String>{
    return this.http.post(`${this.apiServerUrl}/Scan/deleteConfig`, id, {responseType: 'text'});
  }
}
