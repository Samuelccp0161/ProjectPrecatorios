package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.message.ResponseMessage;
import br.gov.al.sefaz.tributario.services.PdfService;
import br.gov.al.sefaz.tributario.services.PrecatorioService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController @RequestMapping("/api")
public class TributarioController {
    final PrecatorioService precatorio;

    final PdfService pdfService;

    @Autowired
    TributarioController(PdfService pdfService, PrecatorioService precatorio) {
        this.precatorio = precatorio;
        this.pdfService = pdfService;
        WebDriverManager.edgedriver().setup();
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(
            @RequestParam("usuario") String usuario,
            @RequestParam("senha") String senha
    ) {
        precatorio.logar(usuario, senha);

        return ResponseEntity.ok(new ResponseMessage("Usuário logado com sucesso!"));
    }

    @PostMapping("/conta")
    public ResponseEntity<ResponseMessage> entrarNaContaGrafica(
            @RequestParam("conta-grafica") String contaGrafica
    ) {
        precatorio.irParaContaGrafica(contaGrafica);

        var message =
                new ResponseMessage("Entrada da conta gráfica '"+ contaGrafica +"' bem sucedida!");

        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/upload/di")
    public ResponseEntity<ResponseMessage> uploadDi(@RequestParam("file") MultipartFile file) throws IOException {
        pdfService.saveDiFile(file);
        return ResponseEntity.ok(new ResponseMessage("Arquivo lido com sucesso!"));
    }

    @PostMapping("/upload/dmi")
    public ResponseEntity<ResponseMessage> uploadDmi(@RequestParam("file") MultipartFile file) throws IOException {
        pdfService.saveDmiFile(file);
        return ResponseEntity.ok(new ResponseMessage("Arquivo lido com sucesso!"));
    }

    @PostMapping("/submit")
    public ResponseEntity<ResponseMessage> submeter() {
        var dados = pdfService.extrairDados();

        precatorio.focarJanela();
        precatorio.preencherCampos(dados);

        return ResponseEntity.ok().body(new ResponseMessage("Campos preenchidos com sucesso!"));
    }
}
