package br.gov.al.sefaz.tributario.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class PaginaPrecatorio {
    private final ChromeOptions options;
    private WebDriver driver;

    private PaginaPrecatorio(ChromeOptions options) {
        this.driver = criarDriver(options);
        this.options = options;
        minimizar();
    }

    private WebDriver criarDriver(ChromeOptions options) {
        return new ChromeDriver(options);
    }

    private static ChromeOptions criarOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-dev-shm-usage");
        return options;
    }

    public static PaginaPrecatorio criar() {
        return new PaginaPrecatorio(criarOptions());
    }

    public static PaginaPrecatorio criarHeadless() {
        return new PaginaPrecatorio(criarOptions().setHeadless(true));
    }

    public void abrir(){
        try {
            driver.get("https://precatorios.sefaz.al.gov.br/");
            driver.switchTo().frame(driver.findElement(By.name("principal")));
        } catch (Exception e) {
            this.driver = criarDriver(options);
            minimizar();
            driver.get("https://precatorios.sefaz.al.gov.br/");
            driver.switchTo().frame(driver.findElement(By.name("principal")));
        }
    }

    public void logar(String usuario, String senha){
        driver.findElement(By.id("txtLogin")).sendKeys(usuario);
        driver.findElement(By.id("txtSenha")).sendKeys(senha);
        driver.findElement(By.id("btnEntrar")).click();
    }

    public boolean isLogado() {
        try {
            return driver.findElement(By.linkText("Sair")) != null;
        }
        catch (NoSuchElementException ignore) {
            return false;
        }

    }

    public boolean isEmContaGrafica(){
        try {
            return driver.findElement(By.linkText("Cadastrar")) != null;
        } catch (NoSuchElementException ignore) {
            return false;
        }
    }

    public void irParaContaGrafica(String contaGrafica) {
        driver.findElement(By.id("mi_0_5")).click();
        driver.findElement(By.id("mi_0_6")).click();
        WebElement campoContaGrafica = driver.findElement(By.id("contaGrafica"));
        campoContaGrafica.sendKeys(contaGrafica);
        campoContaGrafica.sendKeys(Keys.ENTER);
    }

    public void close() {
        driver.close();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void preencherCampoPorID(String id, String valor) {
        driver.findElement(By.id(id)).sendKeys(valor);
    }

    public void minimizar() {
        driver.manage().window().minimize();
    }
    public void maximizar() {
        driver.manage().window().maximize();
    }
}
