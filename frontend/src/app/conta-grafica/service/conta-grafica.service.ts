import { Observable, catchError, of } from 'rxjs';
import { Message } from 'src/app/shared/message';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ContaGraficaService {
  api = '/api/conta'
  constructor(private http: HttpClient) { }

  entrar(form: FormData): Observable<Message> {
    return this.http.post<Message>(this.api, form).pipe(
      catchError(err => {return of(err.error)})
    );
  }
}
