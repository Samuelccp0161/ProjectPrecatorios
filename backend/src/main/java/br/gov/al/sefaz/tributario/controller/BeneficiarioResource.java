package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.vo.ResponseMessage;
import br.gov.al.sefaz.tributario.service.BeneficiarioService;
import br.gov.al.sefaz.tributario.service.PdfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController @RequestMapping("/api/beneficiario")
public class BeneficiarioResource {

    private final BeneficiarioService beneficiarioService;
    private final PdfService pdfService;

    public BeneficiarioResource(BeneficiarioService beneficiarioService, PdfService pdfService) {
        this.beneficiarioService = beneficiarioService;
        this.pdfService = pdfService;
    }

    @PostMapping
    public void inserirNumeroProcesso(@RequestBody String numeroProcesso) {
        beneficiarioService.inserirNumeroProcesso(numeroProcesso);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadDmi(@RequestParam("file") MultipartFile file) {
        pdfService.saveFileBeneficiario(file);
        return ResponseEntity.ok(new ResponseMessage("Arquivo lido com sucesso!"));
    }

    @PostMapping("/submit")
    public ResponseEntity<ResponseMessage> submeter() {
        var dados = pdfService.extrairDadosBeneficiario();

        beneficiarioService.preencherDados(dados);

        return ResponseEntity.ok().body(new ResponseMessage("Campos preenchidos com sucesso!"));
    }
}
