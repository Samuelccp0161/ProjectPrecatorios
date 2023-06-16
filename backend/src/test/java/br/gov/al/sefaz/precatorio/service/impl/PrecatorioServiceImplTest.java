package br.gov.al.sefaz.precatorio.service.impl;

import br.gov.al.sefaz.precatorio.TestUtil;
import br.gov.al.sefaz.precatorio.exception.LoginException;
import br.gov.al.sefaz.precatorio.selenium.FabricaDriver;
import br.gov.al.sefaz.precatorio.service.PrecatorioService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PrecatorioServiceImplTest {

    private PrecatorioServiceImpl precatorioService;
    private String url = "https://precatorios.sefaz.al.gov.br/";

    @BeforeEach
    void criarPagina() {
        precatorioService = new PrecatorioServiceImpl();
        url = TestUtil.configurarAmbientePagina(precatorioService);
        TestUtil.configurarAmbienteWebdriver();
    }

    @AfterEach
    void fecharPagina() {
        precatorioService.close();
        FabricaDriver.setRemoto();
    }

    @Test
    void deveriaIniciarNaPaginaPrecatorios() {
        precatorioService.abrirPagina();

        assertThat(FabricaDriver.obterDriver().getCurrentUrl()).isEqualTo(url);
    }

    @Nested
    public class AoLogar {
        private static final String usuario = "ab";
        private static final String senha = "de";

        @Test
        void comCredenciaisInvalidas() {

            assertThatThrownBy(() -> precatorioService.logar(usuario, senha))
                    .isInstanceOf(LoginException.class)
                    .hasMessageContaining("Usuário ou senha inválidos!");
        }

        @Test
        void comCredenciaisValidas() {
            precatorioService.logar("sdcabral", "Samuka0810");

            WebDriver driver = FabricaDriver.obterDriver();
            assertDoesNotThrow(() -> driver.findElement(By.linkText("Sair")));

            Assertions.assertThat(PrecatorioService.naoLogou()).isFalse();
        }
    }
}
