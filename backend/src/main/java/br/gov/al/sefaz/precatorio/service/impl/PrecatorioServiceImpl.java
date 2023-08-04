package br.gov.al.sefaz.precatorio.service.impl;

import br.gov.al.sefaz.precatorio.exception.LoginException;
import br.gov.al.sefaz.precatorio.selenium.Navegador;
import br.gov.al.sefaz.precatorio.service.PrecatorioService;
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
        Navegador.obterDriver().findElement(By.id("txtLogin")).sendKeys(usuario);
        Navegador.obterDriver().findElement(By.id("txtSenha")).sendKeys(senha);
        Navegador.obterDriver().findElement(By.id("btnEntrar")).click();
    }

    private void mudarParaFramePrincipal() {
        Navegador.obterDriver().switchTo().frame("principal");
    }

    public void close() {
        Navegador.fecharDriver();
    }

    protected void abrirPagina() {
        Navegador.obterNovoDriver().get(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
