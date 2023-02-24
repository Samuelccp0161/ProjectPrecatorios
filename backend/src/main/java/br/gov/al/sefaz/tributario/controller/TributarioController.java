package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.message.ResponseMessage;
import br.gov.al.sefaz.tributario.pdfhandler.PDF;
import br.gov.al.sefaz.tributario.pdfhandler.exception.PdfInvalidoException;
import br.gov.al.sefaz.tributario.services.PrecatorioService;
import br.gov.al.sefaz.tributario.services.PdfService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController @RequestMapping("/api")
public class TributarioController {
    final PdfService pdfService;
    final PrecatorioService precatorio;

    @Autowired
    TributarioController(PdfService pdfService, PrecatorioService precatorio) {
        this.pdfService = pdfService;
        this.precatorio = precatorio;
        WebDriverManager.chromedriver().setup();
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(
            @RequestParam("usuario") String usuario,
            @RequestParam("senha") String senha
    ) {
        precatorio.logar(usuario, senha);

        String message = "Usuário ou senha inválidos!";
        HttpStatus status =  HttpStatus.UNAUTHORIZED;

        if (precatorio.isLogado()) {
            message = "Usuário logado com sucesso!";
            status = HttpStatus.OK;
        }

        return ResponseEntity.status(status).body(new ResponseMessage(message));
    }

    @PostMapping("/conta")
    public ResponseEntity<ResponseMessage> entrarNaContaGrafica(
            @RequestParam("conta-grafica") String contaGrafica
    ) {
        String message = "Conta gráfica inválida!";
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        if (!precatorio.isLogado())
            message = "Usuário não está logado!";
        else {
            precatorio.irParaContaGrafica(contaGrafica);
            if (precatorio.isEmContaGrafica()) {
                message = "Entrada da conta gráfica '"+ contaGrafica +"' bem sucedida!";
                status = HttpStatus.OK;
            }
        }

        return ResponseEntity.status(status).body(new ResponseMessage(message));
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tipo") String tipo)
    {
        String message = "Parametro 'tipo: " + tipo + "' não é válido!";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        try {
            switch (tipo) {
                case "dmi": pdfService.save(file, PDF.Tipo.DMI); break;
                case "di":  pdfService.save(file, PDF.Tipo.DI);  break;
                default:
                    return ResponseEntity.status(status)
                            .body(new ResponseMessage(message));
            }

            message = "Arquivo lido com sucesso!";
            status = HttpStatus.OK;
        }
        catch (PdfInvalidoException e) {
            message = e.getMessage();
            status = HttpStatus.EXPECTATION_FAILED;
        }
        catch (Exception e) {
            message = "Não foi possível fazer o upload do arquivo: " + file.getOriginalFilename() + "!";
            status = HttpStatus.EXPECTATION_FAILED;
        }

        return ResponseEntity.status(status).body(new ResponseMessage(message));
    }

    @PostMapping("/submit")
    public ResponseEntity<ResponseMessage> submeter() {
        String message;
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        if (!precatorio.isLogado())
            return ResponseEntity.status(status).body(new ResponseMessage("Usuário não está logado!"));
        if (!precatorio.isEmContaGrafica())
            return ResponseEntity.status(status).body(new ResponseMessage("Conta gráfica não informada!"));

        try {
            var dados = pdfService.getDadosParaPreencher();

            precatorio.focarJanela();
            precatorio.preencherCampos(dados);

            message = "Campos preenchidos com sucesso!";
            status = HttpStatus.OK;
        }
        catch (Exception e) {
            message = "Não foi possível preencher os campos! Erro: " + e.getMessage();
        }

        return ResponseEntity.status(status).body(new ResponseMessage(message));
    }
}
