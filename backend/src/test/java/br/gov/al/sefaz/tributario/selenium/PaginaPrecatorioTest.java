package br.gov.al.sefaz.tributario.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;


public class PaginaPrecatorioTest {
    static PaginaPrecatorio paginaPrecatorio;
    final String usuario = "sdcabral";
    final String senha = "Samuka0810";

    @BeforeAll
    static void beforeAll() {
//        WebDriverManager.chromedriver().setup();
//        paginaPrecatorio = PaginaPrecatorio.criarHeadlessComChrome();
        WebDriverManager.edgedriver().setup();
        paginaPrecatorio = PaginaPrecatorio.criarHeadlessComEdge();
    }

    @AfterAll
    static void afterAll() {
        paginaPrecatorio.close();
    }

    @Test
    public void logarPaginaPrecatorios(){
        paginaPrecatorio.abrir();

        assertThat(paginaPrecatorio.isLogado()).isFalse();
        paginaPrecatorio.logar(usuario, senha);
        assertThat(paginaPrecatorio.isLogado()).isTrue();
    }

    @Test
    public void logarPaginaPrecatoriosLoginErrado(){
        paginaPrecatorio.abrir();

        assertThat(paginaPrecatorio.isLogado()).isFalse();
        paginaPrecatorio.logar(usuario, senha + "123");
        assertThat(paginaPrecatorio.isLogado()).isFalse();
    }

    @Test
    public void irParaContaGrafica() {
        paginaPrecatorio.abrir();
        paginaPrecatorio.logar(usuario, senha);

        assertThat(paginaPrecatorio.isEmContaGrafica()).isFalse();
        paginaPrecatorio.irParaContaGrafica("512");
        assertThat(paginaPrecatorio.isEmContaGrafica()).isTrue();
    }

    @Test
    public void irParaContaGraficaInvalida() {
        paginaPrecatorio.abrir();
        paginaPrecatorio.logar(usuario, senha);

        assertThat(paginaPrecatorio.isEmContaGrafica()).isFalse();
        paginaPrecatorio.irParaContaGrafica("512ABC");
        assertThat(paginaPrecatorio.isEmContaGrafica()).isFalse();
    }
    @Test
    public void colocarValores() {
        paginaPrecatorio.abrir();
        paginaPrecatorio.logar(usuario, senha);
        paginaPrecatorio.irParaContaGrafica("512");

        String id = "valTotNotaFiscalSaida";
        String valor = "12345";

        WebElement campo = paginaPrecatorio.getDriver().findElement(By.id(id));
        campo.clear();

        paginaPrecatorio.preencherCampoPorID(id, valor);
        assertThat(campo.getAttribute("value")).isEqualTo(valor);
    }
}
