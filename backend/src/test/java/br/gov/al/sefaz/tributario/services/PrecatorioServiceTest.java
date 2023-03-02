package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.exception.ContaGraficaInvalidaException;
import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.selenium.FabricaPaginaPrecatorio;
import br.gov.al.sefaz.tributario.selenium.PaginaPrecatorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class PrecatorioServiceTest {
    @Mock
    private FabricaPaginaPrecatorio mockFabrica;

    @Mock
    private PaginaPrecatorio mockPagina;

    @InjectMocks
    private PrecatorioService precatorio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        given(mockFabrica.getPagina()).willReturn(mockPagina);
    }

    @Nested
    public class AoLogar {
        private static final String usuario = "abc";
        private static final String senha = "def";

        @Test
        void comCredenciaisInvalidas() {
            given(mockPagina.isLogado()).willReturn(false);

            assertThatThrownBy(() -> precatorio.logar(usuario, senha))
                    .isInstanceOf(LoginException.class)
                    .hasMessageContaining("Usuário ou senha inválidos!");

            then(mockPagina).should(times(1)).logar(usuario,senha);
        }

        @Test
        void comCredenciaisValidas() {
            given(mockPagina.isLogado()).willReturn(true);
            assertDoesNotThrow(() -> precatorio.logar(usuario, senha));
            then(mockPagina).should(times(1)).logar(usuario,senha);
        }
    }

    @Nested
    public class AoTentarEntrarNaContaGrafica {

        private static final String contaGrafica = "123";

        @Test
        void semEstarLogado() {
            given(mockPagina.isLogado()).willReturn(false);

            assertThatThrownBy(() -> precatorio.irParaContaGrafica(contaGrafica))
                    .isInstanceOf(LoginException.class)
                    .hasMessageContaining("Usuário não está logado!");
        }

        @Test
        void comContaInvalida() {
            given(mockPagina.isLogado()).willReturn(true);
            given(mockPagina.isEmContaGrafica()).willReturn(false);

            assertThatThrownBy(() -> precatorio.irParaContaGrafica(contaGrafica))
                    .isInstanceOf(ContaGraficaInvalidaException.class)
                    .hasMessageContaining("Conta gráfica inválida!");

            then(mockPagina).should(times(1)).irParaContaGrafica(contaGrafica);
        }

        @Test
        void comContaValida() {
            given(mockPagina.isLogado()).willReturn(true);
            given(mockPagina.isEmContaGrafica()).willReturn(true);

            assertDoesNotThrow(() -> precatorio.irParaContaGrafica(contaGrafica));
            then(mockPagina).should(times(1)).irParaContaGrafica(contaGrafica);

        }
    }

    @Nested
    public class AoTentarPreencherOsCampos {
        private final Map<String, String> dados = Map.of(
                "id_1", "v1",
                "id_2", "v2",
                "id_3", "v3"
        );
        @Test
        void semEstarLogado() {
            given(mockPagina.isLogado()).willReturn(false);

            assertThatThrownBy(() -> precatorio.preencherCampos(dados))
                    .isInstanceOf(LoginException.class)
                    .hasMessageContaining("Usuário não está logado!");
        }

        @Test
        void semDarEntradaNaContaGrafica() {
            given(mockPagina.isLogado()).willReturn(true);
            given(mockPagina.isEmContaGrafica()).willReturn(false);

            assertThatThrownBy(() -> precatorio.preencherCampos(dados))
                    .isInstanceOf(ContaGraficaInvalidaException.class)
                    .hasMessageContaining("Conta gráfica não informada!");
        }

        @Test
        void comSucesso() {
            given(mockPagina.isLogado()).willReturn(true);
            given(mockPagina.isEmContaGrafica()).willReturn(true);

            assertDoesNotThrow(() -> precatorio.preencherCampos(dados));

            for (var pair : dados.entrySet()) {
                then(mockPagina).should(times(1))
                        .preencherCampoPorID(pair.getKey(), pair.getValue());
            }
        }
    }
}