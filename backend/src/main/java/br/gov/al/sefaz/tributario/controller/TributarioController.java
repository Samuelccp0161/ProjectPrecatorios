package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.message.ResponseMessage;
import br.gov.al.sefaz.tributario.pdfhandler.PDF;
import br.gov.al.sefaz.tributario.pdfhandler.exception.PdfInvalidoException;
import br.gov.al.sefaz.tributario.selenium.PaginaPrecatorio;
import br.gov.al.sefaz.tributario.service.FileStorageService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController @RequestMapping("/api")
public class TributarioController {
    final FileStorageService storageService;

    TributarioController(FileStorageService storageService) {
        this.storageService = storageService;
        WebDriverManager.chromedriver().setup();
    }

    private PaginaPrecatorio paginaPrecatorio;

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(
            @RequestParam("usuario") String usuario,
            @RequestParam("senha") String senha
    ) {
        if (paginaPrecatorio == null)
            paginaPrecatorio = PaginaPrecatorio.criar();

        paginaPrecatorio.abrir();
        paginaPrecatorio.logar(usuario, senha);

        String message = "Usuário ou senha inválidos!";
        HttpStatus status =  HttpStatus.UNAUTHORIZED;

        if (paginaPrecatorio.isLogado()) {
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
        HttpStatus status = HttpStatus.EXPECTATION_FAILED;

        if (paginaPrecatorio == null || !paginaPrecatorio.isLogado())
            message = "Usuário não está logado!";
        else {
            paginaPrecatorio.irParaContaGrafica(contaGrafica);
            if (paginaPrecatorio.isEmContaGrafica()) {
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
                case "dmi": storageService.save(file, PDF.Tipo.DMI); break;
                case "di":  storageService.save(file, PDF.Tipo.DI);  break;
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
    public ResponseEntity<ResponseMessage> preencherCampos() {
        String message = "Conta gráfica não informada!";
        HttpStatus status = HttpStatus.EXPECTATION_FAILED;

        if (paginaPrecatorio == null || !paginaPrecatorio.isEmContaGrafica())
            return ResponseEntity.status(status).body(new ResponseMessage(message));

        try {
            PDF di = storageService.loadDi();
            PDF dmi = storageService.loadDmi();

            Map<String, String> dados = new HashMap<>();
            dados.putAll(di.getTabela());
            dados.putAll(dmi.getTabela());

            paginaPrecatorio.minimizar();
            paginaPrecatorio.maximizar();

            for (Map.Entry<String,String> pair : dados.entrySet())
                paginaPrecatorio.preencherCampoPorID(pair.getKey(), pair.getValue());
            paginaPrecatorio.zerarPorcentagemICMSrecolher();
            paginaPrecatorio.clicarCampoNotaFiscal();
            message = "Campos preenchidos com sucesso!";
            status = HttpStatus.OK;
        }
        catch (Exception e) {
            message = "Não foi possível preencher os campos! Erro: " + e.getMessage();
        }

        return ResponseEntity.status(status).body(new ResponseMessage(message));
    }
}
