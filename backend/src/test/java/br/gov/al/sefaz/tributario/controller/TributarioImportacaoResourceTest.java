package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.exception.ContaGraficaInvalidaException;
import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.exception.handler.CustomExceptionHandler;
import br.gov.al.sefaz.tributario.exception.PdfInvalidoException;
import br.gov.al.sefaz.tributario.service.PdfService;
import br.gov.al.sefaz.tributario.service.TributarioImportacaoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TributarioImportacaoResourceTest {
    public static final String API_URL = "/api/tributario-importacao";
    protected MockMvc mvc;

    @Mock
    protected TributarioImportacaoService tributarioService;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private TributarioImportacaoResource tributarioImportacaoResource;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(tributarioImportacaoResource)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Nested @DisplayName("Ao inserir Conta Grafica")
    public class AoInserirContaGrafica {
        String contaGrafica = "12";

        @Test @DisplayName("sem estar logado")
        void semEstarLogado() throws Exception {
            var loginException = new LoginException("Usuário não está logado!");
            doThrow(loginException).when(tributarioService).irParaContaGrafica(contaGrafica);

            mvc.perform(post(API_URL + "/" + contaGrafica))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio nÃ£o estÃ¡ logado!")));

            verify(tributarioService).irParaContaGrafica(contaGrafica);
        }

        @Test @DisplayName("com conta invalida")
        void comContaInvalida() throws Exception {
            var contaGraficaException = new ContaGraficaInvalidaException("Conta gráfica inválida!");
            doThrow(contaGraficaException).when(tributarioService).irParaContaGrafica(contaGrafica);

            mvc.perform(post(API_URL + "/" + contaGrafica))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(
                            containsString("Conta grÃ¡fica invÃ¡lida!")));

            verify(tributarioService).irParaContaGrafica(contaGrafica);
        }

        @Test @DisplayName("com conta valida")
        void comContaValida() throws Exception {
            mvc.perform(post(API_URL + "/" + contaGrafica))
                    .andDo(print())
                    .andExpect(status().isOk());

            verify(tributarioService).irParaContaGrafica(contaGrafica);
        }
    }

    @Nested
    public class AoTentarFazerUploadDosArquivos {
        private final MockMultipartFile mockFile =
                new MockMultipartFile("file", "Arquivo Mockado".getBytes());

        @Test
        void comDiValido() throws Exception {
            mvc.perform(multipart(API_URL + "/upload/di").file(mockFile))
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

            mvc.perform(multipart(API_URL + "/upload/di").file(mockFile))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string(containsString("O Arquivo enviado nÃ£o Ã© um 'DI' vÃ¡lido")))
            ;

            verify(pdfService).saveDiFile(mockFile);
        }

        @Test
        void comDmiValido() throws Exception {
            mvc.perform(multipart(API_URL + "/upload/dmi").file(mockFile))
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

            mvc.perform(multipart(API_URL + "/upload/dmi").file(mockFile))
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
            given(pdfService.extrairDadosTributario()).willReturn(dados);
        }

        @Test
        void semEstarLogado() throws Exception {
            var loginException = new LoginException("Usuário não está logado!");
            doThrow(loginException).when(tributarioService).preencherDados(dados);

            mvc.perform(post(API_URL + "/submit"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio nÃ£o estÃ¡ logado!")));
        }

        @Test
        void semDarEntradaNaContaGrafica() throws Exception {
            var contaGraficaException =
                    new ContaGraficaInvalidaException("Conta gráfica não informada!");
            doThrow(contaGraficaException).when(tributarioService).preencherDados(dados);

            mvc.perform(post(API_URL + "/submit"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString("Conta grÃ¡fica nÃ£o informada!")));
        }

        @Test
        void comSucesso() throws Exception {
            mvc.perform(post(API_URL + "/submit"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Campos preenchidos com sucesso!")));

            verify(tributarioService).preencherDados(dados);
        }
    }
}
