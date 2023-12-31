package br.gov.al.sefaz.precatorio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContaGraficaInvalidaException extends RuntimeException {
    public ContaGraficaInvalidaException(String msg) {
        super(msg);
    }
}
