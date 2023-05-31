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

    private static boolean isRemoto = true;
    private static WebDriver webdriver;

    public static WebDriver obterDriver() {
        if (webdriver == null)
            webdriver = criarWebdriver();
        return webdriver;
    }

    public static WebDriver criarWebdriver() {
        closeDriver();
        webdriver = (isRemoto)? criarWebdriverRemoto() : criarWebdriverLocal();

        return webdriver;
    }

    private static void closeDriver() {
        try { webdriver.quit(); }
        catch (Exception ignore) {}
    }

    private static WebDriver criarWebdriverRemoto() {
        esperarDriverRemoto();

        var driver = RemoteWebDriver.builder()
                .address(WEBDRIVER_REMOTE_URL)
                .addAlternative(Options.chrome())
                .addAlternative(Options.firefox())
                .addAlternative(Options.edge())
                .build();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        return driver;
    }

    private static WebDriver criarWebdriverLocal() {
        var driver = new ChromeDriver(Options.chrome().setHeadless(true));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

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

    public static void setRemoto() {
        FabricaDriver.isRemoto = true;
    }

    public static void setLocal() {
        FabricaDriver.isRemoto = false;
    }

    public static boolean isRemoto() {
        return isRemoto;
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
            return new EdgeOptions();
        }

        private static FirefoxOptions firefox() {
            return new FirefoxOptions();
        }
    }
}
