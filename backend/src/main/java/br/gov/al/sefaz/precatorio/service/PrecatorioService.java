package br.gov.al.sefaz.precatorio.service;

import br.gov.al.sefaz.precatorio.selenium.Navegador;
import org.openqa.selenium.By;

public interface PrecatorioService {
    void logar(String usuario, String senha);

    static boolean naoLogou() {
        try {
            return Navegador.obterDriver().findElement(By.linkText("Sair")) == null;
        }
        catch (Exception ignore) {
            return true;
        }
    }
}
