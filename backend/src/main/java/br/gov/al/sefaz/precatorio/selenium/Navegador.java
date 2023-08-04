package br.gov.al.sefaz.precatorio.selenium;

import org.openqa.selenium.WebDriver;

public class Navegador {

    private static Ambiente ambiente = Ambiente.DOCKER;
    private static FabricaChromedriver fabrica = FabricaChromedriver.fabricaDocker();
    private static WebDriver webDriver;

    public static WebDriver obterDriver() {
        if (webDriver == null)
            webDriver = fabrica.criarDriver();

        return webDriver;
    }

    public static WebDriver obterNovoDriver() {
        fecharDriver();
        return obterDriver();
    }

    public static void fecharDriver() {
        try { webDriver.quit(); }
        catch (Exception ignore) {}
        finally { webDriver = null; }
    }

    public static void setAmbienteToLocal() {
        ambiente = Ambiente.LOCAL;
        fabrica = FabricaChromedriver.fabricaLocal();
    }

    public static void setAmbienteToHost() {
        ambiente = Ambiente.HOST;
        fabrica = FabricaChromedriver.fabricaHost();
    }

    public static void setAmbienteToDocker() {
        ambiente = Ambiente.DOCKER;
        fabrica = FabricaChromedriver.fabricaDocker();
    }

    public static boolean isLocal() {
        return ambiente == Ambiente.LOCAL;
    }

    enum Ambiente {LOCAL, HOST, DOCKER}
}
