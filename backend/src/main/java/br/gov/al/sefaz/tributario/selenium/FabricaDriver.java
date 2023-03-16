package br.gov.al.sefaz.tributario.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

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

    public WebDriver criarDriver() {
        if (navegador == Navegador.Edge) {
            return criarEdgeDriver(edgeOptions);
        }
        return criarChromeDriver(chromeOptions);
    }

    public enum Navegador {Chrome, Edge}

    private static WebDriver criarChromeDriver(ChromeOptions options) {
        return new ChromeDriver(options);
    }

    public static WebDriver criarEdgeDriver(EdgeOptions options) {
        return new EdgeDriver(options);
    }
}
