package br.gov.al.sefaz.precatorio.exception;

public class PdfInvalidoException extends RuntimeException {
    public PdfInvalidoException(String msg) {
        super(msg);
    }

    public PdfInvalidoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
