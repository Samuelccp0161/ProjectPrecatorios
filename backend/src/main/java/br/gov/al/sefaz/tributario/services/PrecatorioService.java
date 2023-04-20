package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.selenium.FabricaDriver;
import org.openqa.selenium.By;

public interface PrecatorioService {
    void logar(String usuario, String senha);

    static boolean naoLogou() {
        try {
            return FabricaDriver.obterDriver().findElement(By.linkText("Sair")) == null;
        }
        catch (Exception ignore) {
            return true;
        }
    }
}
