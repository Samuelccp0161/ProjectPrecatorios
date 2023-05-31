import { Component, OnInit } from '@angular/core';
import {DataService} from "../../shared/services/data.service";
import {Router} from "@angular/router";
import {PdfInputFile} from "../../shared/pdf-file";
import {TributarioUploadService} from "./service/tributario-upload.service";

@Component({
  selector: 'app-upload',
  templateUrl: './tributario-upload.component.html',
  styleUrls: ['./tributario-upload.component.scss']
})
export class TributarioUploadComponent implements OnInit {
  dmi = new PdfInputFile();
  di = new PdfInputFile();

  loading = false;

  contaGrafica!: string;

  constructor(
      private data: DataService,
      private router: Router,
      private uploadService: TributarioUploadService
  ) {}

  ngOnInit(): void {
    if (this.data.getContaGrafica() === "")
      this.router.navigateByUrl("/precatorio");

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
    pdf.setFile(target.files!.item(0));
  }

  enviarPdfs(): void {
    if (this.di.file == null || this.dmi.file == null) return;

    if (!this.dmi.wasSend) {
      this.uploadService.uploadDmi(this.dmi.file).subscribe((res) => {
        if (res.message != "Arquivo lido com sucesso!") {
          this.dmi.invalidar(res.message);
        } else this.dmi.foiEnviado();
      })
    }

    if (!this.di.wasSend) {
      this.uploadService.uploadDi(this.di.file).subscribe((res) => {
        if (res.message != "Arquivo lido com sucesso!") {
          this.di.invalidar(res.message);
        } else this.di.foiEnviado();
      })
    }
  }

  onSubmit(): void {
    if (this.di.wasSend && this.dmi.wasSend) {
      this.loading = true;
      this.uploadService.submeter().subscribe(() => {
        this.loading = false;
      });
    }
  }
}
