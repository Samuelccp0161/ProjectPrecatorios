import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {LoginComponent} from './login/login.component';
import {PrecatorioComponent} from "./precatorio/precatorio.component";
import {UploadComponent} from "./precatorio/tributario/upload/upload.component";

const routes: Routes = [
  {path: "login", component: LoginComponent},
  {path: "precatorio", component: PrecatorioComponent},
  {path: "precatorio/tributario-upload", component: UploadComponent},
  {path: "", redirectTo: "login", pathMatch: "full"},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
