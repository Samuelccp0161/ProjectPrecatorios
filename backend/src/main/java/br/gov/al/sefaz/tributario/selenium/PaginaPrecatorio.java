package br.gov.al.sefaz.tributario.selenium;

import org.openqa.selenium.*;

public class PaginaPrecatorio {
    private WebDriver driver;
    private final FabricaDriver fabricaDriver;

    public PaginaPrecatorio() {
        this.fabricaDriver = new FabricaDriver();
        this.driver = fabricaDriver.criarDriver();
    }

    public void abrir(){
        try {
            driver.get("https://precatorios.sefaz.al.gov.br/");
            driver.switchTo().frame(driver.findElement(By.name("principal")));
        } catch (Exception e) {
            this.driver = fabricaDriver.criarDriver();
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

    public void zerarPorcentagemICMSrecolher() {
        driver.findElement(By.id("valPorcentagemICMSRecolher")).clear();
    }

    public void clicarCampoNotaFiscal() {
        driver.findElement(By.id("numNotaFiscal")).click();
    }
}
