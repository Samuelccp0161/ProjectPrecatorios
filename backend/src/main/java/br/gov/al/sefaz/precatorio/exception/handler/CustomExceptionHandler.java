package br.gov.al.sefaz.precatorio.exception.handler;

import br.gov.al.sefaz.precatorio.exception.ContaGraficaInvalidaException;
import br.gov.al.sefaz.precatorio.exception.ExceptionResponse;
import br.gov.al.sefaz.precatorio.exception.LoginException;
import br.gov.al.sefaz.precatorio.exception.ProcessoInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(
            Exception ex, WebRequest request
    ) {
        ExceptionResponse response = converterParaExceptionResponse(ex, request);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LoginException.class)
    public final ResponseEntity<ExceptionResponse> handleLoginExceptions(
            Exception ex, WebRequest request)
    {
        ExceptionResponse response = converterParaExceptionResponse(ex, request);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ContaGraficaInvalidaException.class)
    public final ResponseEntity<ExceptionResponse> handleContaInvalidaExceptions(
            Exception ex, WebRequest request)
    {
        ExceptionResponse response = converterParaExceptionResponse(ex, request);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProcessoInvalidoException.class)
    public final ResponseEntity<ExceptionResponse> handleProcessoInvalidoExceptions(
            Exception ex, WebRequest request)
    {
        ExceptionResponse response = converterParaExceptionResponse(ex, request);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private static ExceptionResponse converterParaExceptionResponse(Exception ex, WebRequest request) {
        return new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }
}
