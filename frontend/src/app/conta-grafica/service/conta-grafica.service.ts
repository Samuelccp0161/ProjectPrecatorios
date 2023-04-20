import {Observable, tap} from 'rxjs';
import {Message} from 'src/app/shared/message';
import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ContaGraficaService {
  api = '/api/tributario-importacao'
  constructor(private http: HttpClient) { }

  entrar(conta: string): Observable<Message> {
    return this.http.post<Message>(this.api + '/' + conta, {}).pipe(
        tap(res => console.log(res)),
      // catchError(err => {return of(err.error)})
    );
  }
}
