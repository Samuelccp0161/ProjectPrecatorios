package br.gov.al.sefaz.precatorio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PdfInvalidoException extends RuntimeException {
    public PdfInvalidoException(String msg) {
        super(msg);
    }

    public PdfInvalidoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
