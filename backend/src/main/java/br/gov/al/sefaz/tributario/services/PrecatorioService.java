package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.selenium.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

public interface PrecatorioService {
    void logar(String usuario, String senha);

    static boolean naoLogou() {
        try {
            return Driver.obterDriver().findElement(By.linkText("Sair")) == null;
        }
        catch (NoSuchElementException ignore) {
            return true;
        }
    }
}
