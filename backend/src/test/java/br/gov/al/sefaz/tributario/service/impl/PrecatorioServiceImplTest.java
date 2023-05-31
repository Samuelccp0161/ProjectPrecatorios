package br.gov.al.sefaz.tributario.service.impl;

import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.selenium.FabricaDriver;
import br.gov.al.sefaz.tributario.service.PrecatorioService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PrecatorioServiceImplTest {

    private PrecatorioServiceImpl precatorioService;
    private String url = "https://precatorios.sefaz.al.gov.br/";

    @BeforeEach
    void criarPagina() {
        precatorioService = new PrecatorioServiceImpl();

        var ambiente = System.getProperty("ambiente", "local");
        if (!ambiente.equalsIgnoreCase("remoto")) {
            var paginaLocal = Paths.get("src/test/resources/precatorio-local/precatorio-nao-logado.html")
                    .toAbsolutePath();
            url = "file://" + paginaLocal;
            precatorioService.setUrl(url);
            FabricaDriver.setLocal();
        }

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
