import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ContaGraficaComponent } from './conta-grafica/conta-grafica.component';
import { LoginComponent } from './login/login.component';
import { UploadPdfsComponent } from './upload-pdfs/upload-pdfs.component';

const routes: Routes = [
  {path: "login", component: LoginComponent},
  {path: "grafica", component: ContaGraficaComponent},
  {path: "upload", component: UploadPdfsComponent},
  {path: "", redirectTo: "login", pathMatch: "full"},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
