package br.gov.al.sefaz.tributario.pdfhandler.exception;

import br.gov.al.sefaz.tributario.pdfhandler.PDF;

public class PdfInvalidoException extends RuntimeException {
    public PdfInvalidoException(String msg) {
        super(msg);
    }

    public PdfInvalidoException(PDF.Tipo tipo) {
        this("O Arquivo enviado não é um '" + tipo + "' válido");
    }
}
