package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.selenium.Driver;
import org.openqa.selenium.By;
import org.springframework.stereotype.Service;

@Service
public class PrecatorioServiceImpl implements PrecatorioService {
    @Override
    public void logar(String usuario, String senha) {
        abrirPagina();

        mudarParaFramePrincipal();

        preencherLogin(usuario, senha);

        if (PrecatorioService.naoLogou())
            throw new LoginException("Usuário ou senha inválidos!");
    }

    private void preencherLogin(String usuario, String senha) {
        Driver.obterDriver().findElement(By.id("txtLogin")).sendKeys(usuario);
        Driver.obterDriver().findElement(By.id("txtSenha")).sendKeys(senha);
        Driver.obterDriver().findElement(By.id("btnEntrar")).click();
    }

    private void mudarParaFramePrincipal() {
        Driver.obterDriver().switchTo().frame("principal");
    }

    public void close() {
        Driver.close();
    }

    protected void abrirPagina() {
        Driver.obterNovoDriver().get("https://precatorios.sefaz.al.gov.br/");
    }
}
