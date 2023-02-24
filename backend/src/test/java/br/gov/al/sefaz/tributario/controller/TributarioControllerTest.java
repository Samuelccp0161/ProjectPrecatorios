package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.services.PrecatorioService;
import br.gov.al.sefaz.tributario.services.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TributarioControllerTest {
    protected MockMvc mvc;

    @Mock
    protected PrecatorioService mockPrecatorio;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private TributarioController tributarioController;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(tributarioController).build();
    }

    //Todo: concertar os testes das responses do login. o content não esta em utf-8

    @Nested
    public class AoTentarLogar {
        private static final String params = "?usuario=abc&senha=abc";

        @Test
        public void comLoginValido() throws Exception {
            given(mockPrecatorio.isLogado()).willReturn(true);

            mvc.perform(post("/api/login" + params))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("UsuÃ¡rio logado com sucesso!")));
        }

        @Test
        public void comLoginInvalido() throws Exception {
            given(mockPrecatorio.isLogado()).willReturn(false);

            mvc.perform(post("/api/login" + params))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio ou senha invÃ¡lidos!")));
        }
    }

    @Nested
    public class AoTentarEntrarNaContaGrafica {
        @Test
        void semEstarLogado() throws Exception {
            given(mockPrecatorio.isLogado()).willReturn(false);

            mvc.perform(post("/api/conta?conta-grafica=12"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio nÃ£o estÃ¡ logado!")));
        }

        @Test
        void comContaInvalida() throws Exception {
            given(mockPrecatorio.isLogado()).willReturn(true);
            given(mockPrecatorio.isEmContaGrafica()).willReturn(false);

            mvc.perform(post("/api/conta?conta-grafica=12"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(
                            containsString("Conta grÃ¡fica invÃ¡lida!")));
        }

        @Test
        void comContaValida() throws Exception {
            given(mockPrecatorio.isLogado()).willReturn(true);
            given(mockPrecatorio.isEmContaGrafica()).willReturn(true);

            mvc.perform(post("/api/conta?conta-grafica=12"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                            containsString("Entrada da conta grÃ¡fica '12' bem sucedida!")));
        }
    }

    @Nested
    public class AoSubmeter {

        @Test
        void semEstarLogado() throws Exception {
            given(mockPrecatorio.isLogado()).willReturn(false);

            mvc.perform(post("/api/submit"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio nÃ£o estÃ¡ logado!")));
        }

        @Test
        void semDarEntradaNaContaGrafica() throws Exception {
            given(mockPrecatorio.isLogado()).willReturn(true);
            given(mockPrecatorio.isEmContaGrafica()).willReturn(false);

            mvc.perform(post("/api/submit"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("Conta grÃ¡fica nÃ£o informada!")));
        }

        @Test
        void comSucesso() throws Exception {
            var dados = new HashMap<String, String>();

            dados.put("id_1", "valor_1");
            dados.put("id_2", "valor_2");
            dados.put("id_3", "valor_3");

            given(mockPrecatorio.isLogado()).willReturn(true);
            given(mockPrecatorio.isEmContaGrafica()).willReturn(true);

            given(pdfService.getDadosParaPreencher()).willReturn(dados);

            mvc.perform(post("/api/submit"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Campos preenchidos com sucesso!")));

            then(mockPrecatorio).should(times(1)).preencherCampos(dados);
        }
    }
}