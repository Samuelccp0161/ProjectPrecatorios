import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {Message} from "../../../shared/message";

@Injectable({
  providedIn: 'root'
})
export class TributarioService {
  api = '/api/tributario-importacao'
  constructor(private http: HttpClient) { }

  entrar(conta: string): Observable<Message> {
    return this.http.post<Message>(this.api + '/' + conta, {}).pipe(
        tap(res => console.log(res)),
        // catchError(err => {return of(err.error)})
    );
  }
}
