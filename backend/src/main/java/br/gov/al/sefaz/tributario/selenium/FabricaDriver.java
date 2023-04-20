package br.gov.al.sefaz.tributario.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;

public class FabricaDriver {
    public static final String WEBDRIVER_REMOTE_URL = "http://localhost:4444";
    private static WebDriver webdriver;

    public static WebDriver obterDriver() {
        if (webdriver == null)
            webdriver = criarWebdriver();
        return webdriver;
    }

    public static WebDriver obterNovoDriver() {
        closeDriver();

        webdriver = criarWebdriver();
        return webdriver;
    }

    private static void closeDriver() {
        try { webdriver.quit(); }
        catch (Exception ignore) {}
    }

    private static WebDriver criarWebdriver() {
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

    public static void close() {
        closeDriver();
        webdriver = null;
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
