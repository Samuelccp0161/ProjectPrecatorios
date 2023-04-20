import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, of, tap} from 'rxjs';
import {Message} from 'src/app/shared/message';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
    api = 'api/login'

    constructor(private http: HttpClient) { }

    logar(usuario: string, senha: string): Observable<Message> {
        let body = {usuario, senha}

        return this.http.post<Message>(this.api, body).pipe(
            tap(res => { console.log(res) }),
        )
    }
}
