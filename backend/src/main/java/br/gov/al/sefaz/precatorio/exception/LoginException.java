package br.gov.al.sefaz.precatorio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LoginException(String msg) {
        super(msg);
    }
}
