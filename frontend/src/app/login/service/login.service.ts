import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of } from 'rxjs';
import { Message } from 'src/app/shared/message';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  api = 'api/login'

  constructor(private http: HttpClient) { }

  logar(form: FormData): Observable<Message> {
    return this.http.post<Message>(this.api, form).pipe(
      catchError(err => {
        if (err.status == 404 || err.status == 504) {
          let msg = new Message("Não foi possivel conectar!");
          err.error = msg;
        }
        return of(err.error)
      })
    )
  }
}
