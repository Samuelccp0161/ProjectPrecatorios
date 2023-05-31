package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.vo.ResponseMessage;
import br.gov.al.sefaz.tributario.service.PdfService;
import br.gov.al.sefaz.tributario.service.TributarioImportacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController @RequestMapping("/api/tributario-importacao")
public class TributarioImportacaoResource {
    final TributarioImportacaoService tributarioService;
    final PdfService pdfService;

    TributarioImportacaoResource(PdfService pdfService, TributarioImportacaoService tributarioService) {
        this.pdfService = pdfService;
        this.tributarioService = tributarioService;
    }

    @PostMapping("/{conta}")
    public void inserirContaGrafica(@PathVariable String conta) {
        tributarioService.irParaContaGrafica(conta);
    }

    @PostMapping("/upload/di")
    public ResponseEntity<ResponseMessage> uploadDi(@RequestParam("file") MultipartFile file) {
        pdfService.saveDiFile(file);
        return ResponseEntity.ok(new ResponseMessage("Arquivo lido com sucesso!"));
    }

    @PostMapping("/upload/dmi")
    public ResponseEntity<ResponseMessage> uploadDmi(@RequestParam("file") MultipartFile file) {
        pdfService.saveDmiFile(file);
        return ResponseEntity.ok(new ResponseMessage("Arquivo lido com sucesso!"));
    }

    @PostMapping("/submit")
    public ResponseEntity<ResponseMessage> submeter() {
        var dados = pdfService.extrairDadosTributario();

        tributarioService.preencherDados(dados);

        return ResponseEntity.ok().body(new ResponseMessage("Campos preenchidos com sucesso!"));
    }
}
