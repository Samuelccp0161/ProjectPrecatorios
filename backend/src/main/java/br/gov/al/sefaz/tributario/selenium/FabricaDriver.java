package br.gov.al.sefaz.tributario.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

public class FabricaDriver {
    private final Navegador navegador;
    private final ChromeOptions chromeOptions;
    private final EdgeOptions edgeOptions;

    public FabricaDriver(Navegador navegador, ChromeOptions chromeOptions, EdgeOptions edgeOptions) {
        this.navegador = navegador;
        this.chromeOptions = chromeOptions;
        this.edgeOptions = edgeOptions;
    }

    public static FabricaDriver chrome(ChromeOptions options) {
        return new FabricaDriver(Navegador.Chrome, options, null);
    }

    public static FabricaDriver chromeHeadless(ChromeOptions options) {
        return chrome(options.setHeadless(true));
    }

    public static FabricaDriver edge(EdgeOptions options) {
        return new FabricaDriver(Navegador.Edge, null, options);
    }

    public static FabricaDriver edgeHeadless(EdgeOptions options) {
        return edge(options.setHeadless(true));
    }

    public static FabricaDriver firefox() {
        return new FabricaDriver(Navegador.Firefox, null, null);
    }

    public static FabricaDriver firefoxHeadless() {
        return firefox();
    }

    public WebDriver criarDriver() {
        if (navegador == Navegador.Edge) {
            return criarEdgeDriver(edgeOptions);
        } else if (navegador == Navegador.Firefox) {
            return criarFirefoxDriver();
        }
        return criarChromeDriver(chromeOptions);
    }

    private static WebDriver criarFirefoxDriver() {
        FirefoxDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        return driver;
    }

    public enum Navegador {Chrome, Edge, Firefox}

    private static WebDriver criarChromeDriver(ChromeOptions options) {
        return new ChromeDriver(options);
    }

    public static WebDriver criarEdgeDriver(EdgeOptions options) {
        return new EdgeDriver(options);
    }
}
