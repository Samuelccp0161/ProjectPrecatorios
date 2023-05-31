package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.service.PrecatorioService;
import br.gov.al.sefaz.tributario.vo.Login;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api")
public class PrecatorioResource {

    private final PrecatorioService precatorioService;

    public PrecatorioResource(PrecatorioService precatorioService) {
        this.precatorioService = precatorioService;
    }

    @PostMapping("/login")
    public void logar(@RequestBody Login login) {
        precatorioService.logar(login.getUsuario(), login.getSenha());
    }
}
