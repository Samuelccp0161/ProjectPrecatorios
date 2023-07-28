import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Message} from "../../../shared/message";
import {catchError, Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BeneficiarioUploadService {
  apiUpload = "/api/beneficiario/upload";
  apiSubmit = "/api/beneficiario/submit";

  constructor(private http: HttpClient) { }


  upload(file: FormData) {
    return this.http.post<Message>(this.apiUpload, file).pipe(
    //
    );
  }

  submeter(): Observable<Message> {
    return this.http.post<Message>(this.apiSubmit, {}).pipe(
        // catchError((err) => {
        //   return of(err.error);
        // })
    );
  }
}
