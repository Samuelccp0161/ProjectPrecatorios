import { Injectable } from '@angular/core';
import {Observable, tap} from "rxjs";
import {Message} from "../../../shared/message";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BeneficiarioService {
  api = '/api/beneficiario'

  constructor( private http: HttpClient) { }

  entrar(numeroProcesso: string): Observable<Message> {
    return this.http.post<Message>(this.api, numeroProcesso).pipe(
        tap(res => console.log(res)),
        // catchError(err => {return of(err.error)})
    );
  }
}
