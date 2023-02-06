package br.gov.al.sefaz.tributario.pdfhandler.exception;

import br.gov.al.sefaz.tributario.pdfhandler.PDF;

public class PdfInvalidoException extends RuntimeException {
    public PdfInvalidoException(String msg) {
        super(msg);
    }

    public PdfInvalidoException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PdfInvalidoException(PDF.Tipo tipo) {
        this("O Arquivo enviado não é um '" + tipo + "' válido");
    }
    public PdfInvalidoException(PDF.Tipo tipo, Throwable cause) {
        this("O Arquivo enviado não é um '" + tipo + "' válido", cause);
    }
}
