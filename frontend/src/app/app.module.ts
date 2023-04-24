import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppMaterialModule } from './shared/app-material/app-material.module';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderBarComponent } from './header-bar/header-bar.component';
import { LoginComponent } from './login/login.component';
import { ContentHeaderComponent } from './content-header/content-header.component';
import { PrecatorioComponent } from './precatorio/precatorio.component';
import {MatTabsModule} from "@angular/material/tabs";
import { TributarioComponent } from './precatorio/tributario/tributario/tributario.component';
import { BeneficiarioComponent } from './precatorio/beneficiario/beneficiario/beneficiario.component';
import { UploadComponent } from './precatorio/tributario/upload/upload.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderBarComponent,
    LoginComponent,
    ContentHeaderComponent,
    PrecatorioComponent,
    TributarioComponent,
    BeneficiarioComponent,
    UploadComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        HttpClientModule,
        FormsModule,
        AppMaterialModule,
        MatTabsModule,
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
