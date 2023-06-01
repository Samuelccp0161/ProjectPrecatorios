package br.gov.al.sefaz.tributario.service.impl;

import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.selenium.FabricaDriver;
import br.gov.al.sefaz.tributario.service.PrecatorioService;
import org.openqa.selenium.By;
import org.springframework.stereotype.Service;

@Service
public class PrecatorioServiceImpl implements PrecatorioService {
    private String url = "https://precatorios.sefaz.al.gov.br/";

    @Override
    public void logar(String usuario, String senha) {
        abrirPagina();

        mudarParaFramePrincipal();

        preencherLogin(usuario, senha);

        if (PrecatorioService.naoLogou())
            throw new LoginException("Usuário ou senha inválidos!");
    }

    private void preencherLogin(String usuario, String senha) {
        FabricaDriver.obterDriver().findElement(By.id("txtLogin")).sendKeys(usuario);
        FabricaDriver.obterDriver().findElement(By.id("txtSenha")).sendKeys(senha);
        FabricaDriver.obterDriver().findElement(By.id("btnEntrar")).click();
    }

    private void mudarParaFramePrincipal() {
        FabricaDriver.obterDriver().switchTo().frame("principal");
    }

    public void close() {
        FabricaDriver.close();
    }

    protected void abrirPagina() {
        FabricaDriver.criarWebdriver().get(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
