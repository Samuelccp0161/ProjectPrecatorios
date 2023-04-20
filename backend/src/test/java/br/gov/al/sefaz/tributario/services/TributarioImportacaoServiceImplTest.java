package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.exception.ContaGraficaInvalidaException;
import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.selenium.FabricaDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TributarioImportacaoServiceImplTest {

    private TributarioImportacaoServiceImpl pagina;

    @BeforeEach
    void criarPagina() {
        pagina = new TributarioImportacaoServiceImpl();
    }

    @AfterEach
    void fecharPagina() {
        pagina.close();
    }

    @Nested @DisplayName("Ao tentar entrar na conta")
    class AoEntrarNaConta {

        @Nested @DisplayName("sem estar logado")
        class SemEstarLogado {
            @Test @DisplayName("deveria lançar exceção de login")
            public void deveriaLancarExcecao() {
                assertThatThrownBy(() -> pagina.irParaContaGrafica("123"))
                        .isInstanceOf(LoginException.class)
                        .hasMessage("Usuário não está logado!");
            }
        }

        @Nested @DisplayName("apos o login")
        class Logado {
            @BeforeEach
            void fazerLogin() {
                new PrecatorioServiceImpl().logar("sdcabral", "Samuka0810");
            }

            @Test @DisplayName("com conta valida deveria entrar com sucesso")
            public void comContaValida() {
                pagina.irParaContaGrafica("12");

                WebDriver driver = FabricaDriver.obterDriver();
                WebElement contaGrafica = driver.findElement(By.id("sequencialContaGrafica"));

                assertThat(contaGrafica.getAttribute("value")).isEqualTo("12");
            }

            @Test @DisplayName("com conta invalida deveria jogar exceção")
            public void comContaInvalida() {
                assertThatThrownBy(() -> pagina.irParaContaGrafica("abcd"))
                        .isInstanceOf(ContaGraficaInvalidaException.class)
                        .hasMessage("Conta gráfica inválida!");
            }
        }
    }

    @Nested @DisplayName("Ao tentar preencher os campos")
    class AoSubmeter {

        private final Map<String, String> dados = Map.of(
                "numDI", "v1",
                "numNotaFiscal", "v2"
        );

        @Test @DisplayName("sem estar logado deveria lançar exceção de login")
        public void deveriaLancarExcecao() {
            assertThatThrownBy(() -> pagina.preencherDados(dados))
                    .isInstanceOf(LoginException.class)
                    .hasMessage("Usuário não está logado!");
        }

        @Nested @DisplayName("apos o login")
        class Logado {
            @BeforeEach void fazerLogin() {
                new PrecatorioServiceImpl().logar("sdcabral", "Samuka0810");
            }

            @Test @DisplayName("sem entrar na conta deveria lançar exceção de conta invalida")
            public void semContaGrafica() {
                assertThatThrownBy(() -> pagina.preencherDados(dados))
                        .isInstanceOf(ContaGraficaInvalidaException.class)
                        .hasMessage("Conta gráfica não informada!");
            }

            @Test @DisplayName("apos dar entrar na conta deveria preencher com sucesso")
            public void comSucesso() {
                pagina.irParaContaGrafica("512");

                pagina.preencherDados(dados);

                var driver = FabricaDriver.obterDriver();
                for (var pair : dados.entrySet()) {
                    String id = pair.getKey();
                    var campo = driver.findElement(By.id(id));

                    assertThat(campo.getAttribute("value")).isEqualTo(pair.getValue());
                }
            }
        }
    }
}