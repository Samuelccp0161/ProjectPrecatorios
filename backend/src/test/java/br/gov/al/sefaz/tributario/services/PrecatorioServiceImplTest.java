package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.selenium.Driver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PrecatorioServiceImplTest {

    private PrecatorioServiceImpl precatorioService;

    @BeforeEach
    void criarPagina() {
        precatorioService = new PrecatorioServiceImpl();
    }

    @AfterEach
    void fecharPagina() {
        precatorioService.close();
    }

    @Test
    void deveriaIniciarNaPaginaPrecatorios() {
        precatorioService.abrirPagina();

        assertThat(Driver.obterDriver().getCurrentUrl()).isEqualTo("https://precatorios.sefaz.al.gov.br/");
    }

    @Nested
    public class AoLogar {
        private static final String usuario = "abc";
        private static final String senha = "def";

        @Test
        void comCredenciaisInvalidas() {

            assertThatThrownBy(() -> precatorioService.logar(usuario, senha))
                    .isInstanceOf(LoginException.class)
                    .hasMessageContaining("Usuário ou senha inválidos!");
        }

        @Test
        void comCredenciaisValidas() {
            precatorioService.logar("sdcabral", "Samuka0810");

            WebDriver driver = Driver.obterDriver();
            assertDoesNotThrow(() -> driver.findElement(By.linkText("Sair")));

            assertThat(PrecatorioService.naoLogou()).isFalse();
        }
    }
}