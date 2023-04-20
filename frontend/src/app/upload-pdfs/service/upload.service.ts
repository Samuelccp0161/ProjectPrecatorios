import { Message } from "src/app/shared/message";
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, of, Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class UploadService {
  apiUpload = "/api/tributario-importacao/upload";
  apiSubmit = "/api/tributario-importacao/submit";
  
  constructor(private http: HttpClient) {}

  upload(form: FormData): Observable<Message> {
    return this.http.post<Message>(this.apiUpload, form).pipe(
      catchError((err) => {
        return of(err.error);
      })
    );
  }

  uploadDi(form: FormData): Observable<Message> {
    return this.http.post<Message>(this.apiUpload + "/di", form).pipe(
      catchError((err) => {
        return of(err.error);
      })
    );
  }

  uploadDmi(form: FormData): Observable<Message> {
    return this.http.post<Message>(this.apiUpload + "/dmi", form).pipe(
      catchError((err) => {
        return of(err.error);
      })
    );
  }

  submeter(): Observable<Message> {
    return this.http.post<Message>(this.apiSubmit, {}).pipe(
      catchError((err) => {
        return of(err.error);
      })
    );
  }
}
