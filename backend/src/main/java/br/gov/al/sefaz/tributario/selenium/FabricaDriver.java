package br.gov.al.sefaz.tributario.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class FabricaDriver {
    private final Navegador navegador;
    private final Options options;

    public FabricaDriver(Navegador navegador, Options options) {
        this.navegador = navegador;
        this.options = options;
    }

    public static FabricaDriver chrome() {
        return new FabricaDriver(Navegador.Chrome, Options.headful());
    }

    public static FabricaDriver chromeHeadless() {
        return new FabricaDriver(Navegador.Chrome, Options.headless());
    }

    public static FabricaDriver edge() {
        return new FabricaDriver(Navegador.Edge, Options.headful());
    }

    public static FabricaDriver edgeHeadless() {
        return new FabricaDriver(Navegador.Edge, Options.headless());
    }

    public static FabricaDriver firefox() {
        return new FabricaDriver(Navegador.Firefox, Options.headful());
    }

    public static FabricaDriver firefoxHeadless() {
        return new FabricaDriver(Navegador.Firefox, Options.headless());
    }

    public WebDriver criarDriver() {
        switch (navegador) {
            case Firefox: return criarFirefoxDriver();
            case Edge: return criarEdgeDriver();
            default: return criarChromeDriver();
        }
    }

    public enum Navegador {Chrome, Edge, Firefox}

    private WebDriver criarFirefoxDriver() {
        WebDriver driver;
        try {
            driver = new RemoteWebDriver(new URL("http://localhost:4444"), new FirefoxOptions());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        return driver;
    }

    private WebDriver criarChromeDriver() {
        try {
            Thread.sleep(1500);
            return new RemoteWebDriver(new URL("http://localhost:4444"), options.chrome());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public WebDriver criarEdgeDriver() {
        return new EdgeDriver(options.edge());
    }

    static class Options {
        boolean headless;

        Options(boolean headless) {
            this.headless = headless;
        }

        private static Options headful() {
            return new Options(false);
        }

        private static Options headless() {
            return new Options(true);
        }

        private ChromeOptions chrome() {
            var options = new ChromeOptions();
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-dev-shm-usage");
            if (headless) options.addArguments("--headless=new");
            return options;
        }

        private EdgeOptions edge() {
            var options = new EdgeOptions();
            if (headless) options.addArguments("--headless");
            return options;
        }

        private FirefoxOptions firefox() {
            var options = new FirefoxOptions();
            if (headless) options.addArguments("--headless");
            return options;
        }
    }
}
