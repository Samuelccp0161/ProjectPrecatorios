package br.gov.al.sefaz.tributario.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProcessoInvalidoException extends RuntimeException{
    public ProcessoInvalidoException(String msg) {
        super(msg);
    }
}
