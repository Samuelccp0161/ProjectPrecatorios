export class PdfInputFile {
  file: FormData | null = null;
  isValid = false;
  wasSend = false;
  response = '';

  setFile(file: File | null) {
    if (file != null) {
      this.file = new FormData();
      this.file.append('file', file);
    }
    else this.file = null;

    this.isValid = false;
    this.wasSend = false;
  }

  getFilename(): string {
    let file = this.file?.get('file') as File;
    return file?.name || '';
  }

  invalidar(response: string) {
    this.file = null;
    this.isValid = false;
    this.wasSend = false;
    this.response = response;
  }

  foiEnviado() {
    this.isValid = true;
    this.wasSend = true;
    this.response = '';
  }
}
