package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.exception.ContaGraficaInvalidaException;
import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.exception.handler.CustomExceptionHandler;
import br.gov.al.sefaz.tributario.pdfhandler.exception.PdfInvalidoException;
import br.gov.al.sefaz.tributario.services.PdfService;
import br.gov.al.sefaz.tributario.services.PrecatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TributarioControllerTest {
    protected MockMvc mvc;

    @Mock
    protected PrecatorioService precatorioService;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private TributarioController tributarioController;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(tributarioController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    //Todo: concertar os testes das responses do login. o content não esta em utf-8

    @Nested
    public class AoTentarLogar {

        private static final String usuario = "abc";
        private static final String senha = "def";

        private static final String params = "?usuario=" + usuario + "&senha=" + senha;

        @Test
        public void comLoginValido() throws Exception {
            mvc.perform(post("/api/login" + params))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("UsuÃ¡rio logado com sucesso!")));

            verify(precatorioService).logar(usuario, senha);
        }

        @Test
        public void comLoginInvalido() throws Exception {
            var loginException = new LoginException("Usuário ou senha inválidos!");
            doThrow(loginException).when(precatorioService).logar(usuario, senha);

            mvc.perform(post("/api/login" + params))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio ou senha invÃ¡lidos!")));

            verify(precatorioService).logar(usuario, senha);
        }
    }

    @Nested
    public class AoTentarEntrarNaContaGrafica {
        String contaGrafica = "12";

        @Test
        void semEstarLogado() throws Exception {
            var loginException = new LoginException("Usuário não está logado!");
            doThrow(loginException).when(precatorioService).irParaContaGrafica(contaGrafica);

            mvc.perform(post("/api/conta?conta-grafica=" + contaGrafica))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio nÃ£o estÃ¡ logado!")));

            verify(precatorioService).irParaContaGrafica(contaGrafica);
        }

        @Test
        void comContaInvalida() throws Exception {
            var contaGraficaException = new ContaGraficaInvalidaException("Conta gráfica inválida!");
            doThrow(contaGraficaException).when(precatorioService).irParaContaGrafica(contaGrafica);

            mvc.perform(post("/api/conta?conta-grafica=" + contaGrafica))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(
                            containsString("Conta grÃ¡fica invÃ¡lida!")));

            verify(precatorioService).irParaContaGrafica(contaGrafica);
        }

        @Test
        void comContaValida() throws Exception {
            mvc.perform(post("/api/conta?conta-grafica=" + contaGrafica))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                            containsString("Entrada da conta grÃ¡fica '"+ contaGrafica +"' bem sucedida!")));

            verify(precatorioService).irParaContaGrafica(contaGrafica);
        }
    }

    @Nested
    public class AoTentarFazerUploadDosArquivos {
        private final MockMultipartFile mockFile =
                new MockMultipartFile("file", "Arquivo Mockado".getBytes());

        @Test
        void comDiValido() throws Exception {
            mvc.perform(multipart("/api/upload/di").file(mockFile))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Arquivo lido com sucesso!")))
            ;

            verify(pdfService).saveDiFile(mockFile);
        }
        @Test
        void comDiInvalido() throws Exception {
            var pdfException = new PdfInvalidoException("O Arquivo enviado não é um 'DI' válido");
            doThrow(pdfException).when(pdfService).saveDiFile(mockFile);

            mvc.perform(multipart("/api/upload/di").file(mockFile))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string(containsString("O Arquivo enviado nÃ£o Ã© um 'DI' vÃ¡lido")))
            ;

            verify(pdfService).saveDiFile(mockFile);
        }

        @Test
        void comDmiValido() throws Exception {
            mvc.perform(multipart("/api/upload/dmi").file(mockFile))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Arquivo lido com sucesso!")))
            ;

            verify(pdfService).saveDmiFile(mockFile);
        }
        @Test
        void comDmiInvalido() throws Exception {
            var pdfException = new PdfInvalidoException("O Arquivo enviado não é um 'DMI' válido");
            doThrow(pdfException).when(pdfService).saveDmiFile(mockFile);

            mvc.perform(multipart("/api/upload/dmi").file(mockFile))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string(containsString("O Arquivo enviado nÃ£o Ã© um 'DMI' vÃ¡lido")))
            ;

            verify(pdfService).saveDmiFile(mockFile);
        }
    }

    @Nested
    public class AoSubmeter {
        private final Map<String, String> dados = Map.of("1", "1", "2", "2");

        @BeforeEach
        void setUp() {
            given(pdfService.extrairDados()).willReturn(dados);
        }

        @Test
        void semEstarLogado() throws Exception {
            var loginException = new LoginException("Usuário não está logado!");
            doThrow(loginException).when(precatorioService).preencherCampos(dados);

            mvc.perform(post("/api/submit"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio nÃ£o estÃ¡ logado!")));
        }

        @Test
        void semDarEntradaNaContaGrafica() throws Exception {
            var contaGraficaException =
                    new ContaGraficaInvalidaException("Conta gráfica não informada!");
            doThrow(contaGraficaException).when(precatorioService).preencherCampos(dados);

            mvc.perform(post("/api/submit"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString("Conta grÃ¡fica nÃ£o informada!")));
        }

        @Test
        void comSucesso() throws Exception {
            mvc.perform(post("/api/submit"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Campos preenchidos com sucesso!")));

            verify(precatorioService).preencherCampos(dados);
        }
    }
}