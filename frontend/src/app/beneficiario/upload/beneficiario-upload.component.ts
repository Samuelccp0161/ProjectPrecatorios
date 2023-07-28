import {Component, OnInit} from '@angular/core';
import {PdfInputFile} from "../../shared/pdf-file";
import {DataService} from "../../shared/services/data.service";
import {Router} from "@angular/router";
import {BeneficiarioUploadService} from "./service/beneficiario-upload.service";

@Component({
  selector: 'app-beneficiario-upload',
  templateUrl: './beneficiario-upload.component.html',
  styleUrls: ['./beneficiario-upload.component.scss']
})
export class BeneficiarioUploadComponent implements OnInit {

  pdf = new PdfInputFile();

  loading = false;

  numeroProcesso!: string;

  constructor(
      private data: DataService,
      private router: Router,
      private uploadService: BeneficiarioUploadService
  ) {}

  ngOnInit(): void {
    if (this.data.getNumeroProcesso() === "")
      this.router.navigateByUrl("/precatorio");

    this.numeroProcesso = this.data.getNumeroProcesso();
  }

  onFileSelected(event: Event): void {
    if (event == null || event.target == null) return;

    this.selectFile(event);
  }

  selectFile(event: Event) {
    const target = event.target as HTMLInputElement;
    this.pdf.setFile(target.files!.item(0));
  }

  enviarPdf(): void {
    if (this.pdf.file == null || this.pdf.wasSend) return;

    this.uploadService.upload(this.pdf.file).subscribe({
      next: () => this.pdf.foiEnviado(),
      error: (erro) => this.pdf.invalidar(erro.message)
    });
  }

  onSubmit(): void {
    if (this.pdf.wasSend) {
      this.loading = true;
      this.uploadService.submeter().subscribe(() => this.loading = false);
    }
  }

}
