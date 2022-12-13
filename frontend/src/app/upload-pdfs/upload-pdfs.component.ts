import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { DataService } from './../shared/services/data.service';
import { PdfInputFile } from './model/pdf-file';
import { UploadService } from './service/upload.service';

@Component({
  selector: "app-upload-pdfs",
  templateUrl: "./upload-pdfs.component.html",
  styleUrls: ["./upload-pdfs.component.scss"],
})
export class UploadPdfsComponent implements OnInit {
  dmi = new PdfInputFile();
  di = new PdfInputFile();

  loading = false;

  contaGrafica!: string;

  constructor(
    private data: DataService,
    private router: Router,
    private uploadService: UploadService
  ) {}

  ngOnInit(): void {
    if (this.data.getContaGrafica() === "")
      this.router.navigateByUrl("/grafica");

    this.contaGrafica = this.data.getContaGrafica();
  }

  onDmiSelected(event: Event): void {
    if (event == null || event.target == null) return;

    this.selectFile(event, this.dmi);
  }

  onDiSelected(event: Event): void {
    if (event == null || event.target == null) return;

    this.selectFile(event, this.di);
  }

  selectFile(event: Event, pdf: PdfInputFile) {
    const target = event.target as HTMLInputElement;
    pdf.file = target.files!.item(0);
    pdf.wasSend = false;
    pdf.isValid = false;
  }

  enviarPdfs(): void {
    if (this.di.file == null || this.dmi.file == null) return;

    if (!this.dmi.wasSend) this.uploadFile(this.dmi, this.dmi.file, "dmi");

    if (!this.di.wasSend) this.uploadFile(this.di, this.di.file, "di");
  }

  onSubmit(): void {
    if (this.di.wasSend && this.dmi.wasSend) {
      this.loading = true;
      this.uploadService.submeter().subscribe((res) => {
        this.loading = false;
      });
    }
  }

  uploadFile(pdf: PdfInputFile, file: File, tipo: string) {
    let form = new FormData();

    form.append("file", file);
    form.append("tipo", tipo);

    this.loading = true;
    this.uploadService.upload(form).subscribe((res) => {
      if (res.message != "Arquivo lido com sucesso!") {
        pdf.file = null;
        pdf.response = res.message;
        pdf.wasSend = false;
        pdf.isValid = false;
      } else {
        pdf.isValid = true;
        pdf.wasSend = true;
        pdf.response = "";
      }

      this.loading = false;
    });
  }
}
