package br.gov.al.sefaz.precatorio.service.impl;

import br.gov.al.sefaz.precatorio.TestUtil;
import br.gov.al.sefaz.precatorio.exception.LoginException;
import br.gov.al.sefaz.precatorio.exception.ProcessoInvalidoException;
import br.gov.al.sefaz.precatorio.selenium.FabricaDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BeneficiarioServiceImplTest {

    private PrecatorioServiceImpl precatorioService;
    private BeneficiarioServiceImpl pagina;

    @BeforeEach
    void criarPagina() {
        precatorioService = new PrecatorioServiceImpl();
        pagina = new BeneficiarioServiceImpl();

        TestUtil.configurarAmbiente(precatorioService);
    }

    @AfterEach
    void fecharPaginaEResetarFabricaDriver() {
        pagina.close();
        FabricaDriver.setRemoto();
    }

    @Nested @DisplayName("Ao tentar numero do processo")
    class AoInserirNumProcesso {

        @Nested @DisplayName("sem estar logado")
        class SemEstarLogado {
            @Test
            @DisplayName("deveria lançar exceção de login")
            public void deveriaLancarExcecao() {
                assertThatThrownBy(() -> pagina.inserirNumeroProcesso("123"))
                        .isInstanceOf(LoginException.class)
                        .hasMessage("Usuário não está logado!");
            }
        }

        @Nested @DisplayName("apos o login")
        class Logado {
            @BeforeEach
            void fazerLogin() {
                precatorioService.logar("sdcabral", "Samuka0810");
            }

            @Test @DisplayName("com processo valido deveria entrar com sucesso")
            public void comContaValida() {
                pagina.inserirNumeroProcesso("12");

                WebDriver driver = FabricaDriver.obterDriver();

                assertDoesNotThrow(() -> {
                    driver.findElement(By.id("matricula"));
                });
            }

//            @Test @DisplayName("com conta invalida deveria jogar exceção")
//            public void comContaInvalida() {
//                assertThatThrownBy(() -> pagina.irParaContaGrafica("abcd"))
//                        .isInstanceOf(ContaGraficaInvalidaException.class)
//                        .hasMessage("Conta gráfica inválida!");
//            }
        }
    }

    @Nested @DisplayName("Ao tentar preencher os campos")
    class AoSubmeter {

        private final Map<String, String> dados = Map.of(
                "nome", "v1",
                "cpf", "v2"
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
                precatorioService.logar("sdcabral", "Samuka0810");
            }

            @Test @DisplayName("sem entrar na conta deveria lançar exceção de conta invalida")
            public void semContaGrafica() {
                assertThatThrownBy(() -> pagina.preencherDados(dados))
                        .isInstanceOf(ProcessoInvalidoException.class)
                        .hasMessage("Processo não informado!");
            }

            @Test @DisplayName("apos dar entrar na conta deveria preencher com sucesso")
            public void comSucesso() {
                pagina.inserirNumeroProcesso("512");

                pagina.preencherDados(dados);

                var driver = FabricaDriver.obterDriver();
                for (var pair : dados.entrySet()) {
                    String id = pair.getKey();
                    var campo = driver.findElement(By.id(id));

                    assertThat(campo.getAttribute("value")).isEqualTo(pair.getValue());
                }

                var dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                var campoData = driver.findElement(By.id("datTermoQuitacao"));
                assertThat(campoData.getAttribute("value")).isEqualTo(dataAtual);
            }
        }
    }

    @Test
    void name() {
        var dataAtual = LocalDate.now();

        System.out.println(dataAtual.format(DateTimeFormatter.ofPattern("ddMMyyyy")));
    }
}
