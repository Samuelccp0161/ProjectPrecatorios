package br.gov.al.sefaz.precatorio.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;

public abstract class FabricaChromedriver {
    public static final String WEBDRIVER_HOST_URL = "http://localhost:4444";
    public static final String WEBDRIVER_DOCKER_URL = "http://selenium:4444";

    public abstract WebDriver criarDriver();

    public static FabricaChromedriver fabricaLocal() {
        return new Local();
    }

    public static FabricaChromedriver fabricaHost() {
        return new Remoto(WEBDRIVER_HOST_URL);
    }

    public static FabricaChromedriver fabricaDocker() {
        return new Remoto(WEBDRIVER_DOCKER_URL);
    }

    static class Local extends FabricaChromedriver {

        @Override public WebDriver criarDriver() {
            ChromeDriver driver = new ChromeDriver(getChromeOptions());
            return ajustarImplicitWait(driver);
        }

    }
    static class Remoto extends FabricaChromedriver {
        private final String url;

        private Remoto(String url) {
            this.url = url;
        }
        @Override public WebDriver criarDriver() {
            esperarDriverRemoto();
            
            var driver = RemoteWebDriver.builder()
                    .address(url)
                    .addAlternative(getChromeOptions())
                    .build();

            return ajustarImplicitWait(driver);
        }

        private static void esperarDriverRemoto() {
            try {
                Thread.sleep(1500);
            } catch (Exception ignore) {}
        }

    }

    private static ChromeOptions getChromeOptions() {
        var options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-dev-shm-usage");
        return options;
    }
    private static WebDriver ajustarImplicitWait(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        return driver;
    }
}
