package br.gov.al.sefaz.tributario.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;

public class FabricaDriver {
    public static final String WEBDRIVER_REMOTE_URL = "http://localhost:4444";

    public WebDriver criarDriver() {
        String ambiente = System.getProperty("ambiente.teste");

        if (ambiente == null) {
            return criarDriverRemoto();
        }

        return criarChromeDriverLocal();
    }

    private WebDriver criarChromeDriverLocal() {
        return new ChromeDriver(Options.chrome());
    }

    private WebDriver criarDriverRemoto() {
        esperarDriverRemoto();

        var driver = RemoteWebDriver.builder()
                .address(WEBDRIVER_REMOTE_URL)
                .addAlternative(Options.chrome())
                .addAlternative(Options.firefox())
                .addAlternative(Options.edge())
                .build();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        return driver;
    }

    private static void esperarDriverRemoto() {
        try {
            Thread.sleep(1500);
        } catch (Exception ignore) {}
    }

    static class Options {
        private static ChromeOptions chrome() {
            var options = new ChromeOptions();
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-dev-shm-usage");
            return options;
        }

        private static EdgeOptions edge() {
            var options = new EdgeOptions();
            return options;
        }

        private static FirefoxOptions firefox() {
            var options = new FirefoxOptions();
            return options;
        }
    }
}
