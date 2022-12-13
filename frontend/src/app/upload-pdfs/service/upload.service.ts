import { Message } from "src/app/shared/message";
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, of, Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class UploadService {
  apiUpload = "/api/upload";
  apiSubmit = "/api/submit";
  
  constructor(private http: HttpClient) {}

  upload(form: FormData): Observable<Message> {
    return this.http.post<Message>(this.apiUpload, form).pipe(
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
