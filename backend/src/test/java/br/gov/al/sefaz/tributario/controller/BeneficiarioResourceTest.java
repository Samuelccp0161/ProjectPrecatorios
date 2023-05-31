package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.exception.ProcessoInvalidoException;
import br.gov.al.sefaz.tributario.exception.handler.CustomExceptionHandler;
import br.gov.al.sefaz.tributario.exception.PdfInvalidoException;
import br.gov.al.sefaz.tributario.service.BeneficiarioService;
import br.gov.al.sefaz.tributario.service.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class BeneficiarioResourceTest {
    public static final String API_URL = "/api/beneficiario";
    protected MockMvc mvc;

    @Mock
    protected BeneficiarioService beneficiarioService;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private BeneficiarioResource beneficiarioResource;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(beneficiarioResource)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Nested @DisplayName("Ao inserir numero de processo")
    public class AoInserirNumProcesso {
        private final String numeroProcesso = "123456";

        @Test @DisplayName("sem estar logado")
        void semEstarLogado() throws Exception {
            var loginException = new LoginException("Usuário não está logado!");
            doThrow(loginException).when(beneficiarioService).inserirNumeroProcesso(numeroProcesso);

            mvc.perform(post(API_URL + "/")
                            .content(numeroProcesso)
                            .characterEncoding("utf-8")
                    ).andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio nÃ£o estÃ¡ logado!")));

            verify(beneficiarioService).inserirNumeroProcesso(numeroProcesso);
        }

        @Test @DisplayName("com numero invalido")
        void comNumeroInvalido() throws Exception {
            var processoInvalidoException = new ProcessoInvalidoException("Processo inválido!");
            doThrow(processoInvalidoException)
                    .when(beneficiarioService).inserirNumeroProcesso(numeroProcesso);

            mvc.perform(post(API_URL + "/")
                            .content(numeroProcesso)
                            .characterEncoding("utf-8"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(
                            containsString("Processo invÃ¡lido!")));

            verify(beneficiarioService).inserirNumeroProcesso(numeroProcesso);
        }

        @Test @DisplayName("com numero valido")
        void comNumeroValido() throws Exception {
            mvc.perform(post(API_URL + "/")
                            .content(numeroProcesso)
                            .characterEncoding("utf-8"))
                    .andDo(print())
                    .andExpect(status().isOk());

            verify(beneficiarioService).inserirNumeroProcesso(numeroProcesso);
        }
    }

    @Nested
    public class AoTentarFazerUploadDoArquivo {
        private final MockMultipartFile mockFile =
                new MockMultipartFile("file", "Arquivo Mockado".getBytes());

        @Test
        void comArquivoValido() throws Exception {
            mvc.perform(multipart(API_URL + "/upload").file(mockFile))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Arquivo lido com sucesso!")))
            ;

            verify(pdfService).saveFileBeneficiario(mockFile);
        }
        @Test
        void comArquivoInvalido() throws Exception {
            var pdfException = new PdfInvalidoException("O Arquivo enviado não é válido");
            doThrow(pdfException).when(pdfService).saveFileBeneficiario(mockFile);

            mvc.perform(multipart(API_URL + "/upload").file(mockFile))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string(containsString("O Arquivo enviado nÃ£o Ã© vÃ¡lido")))
            ;

            verify(pdfService).saveFileBeneficiario(mockFile);
        }

    }

    @Nested
    public class AoSubmeter {
        private final Map<String, String> dados = Map.of("1", "1", "2", "2");

        @BeforeEach
        void setUp() {
            given(pdfService.extrairDadosBeneficiario()).willReturn(dados);
        }

        @Test
        void semEstarLogado() throws Exception {
            var loginException = new LoginException("Usuário não está logado!");
            doThrow(loginException).when(beneficiarioService).preencherDados(dados);

            mvc.perform(post(API_URL + "/submit"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio nÃ£o estÃ¡ logado!")));
        }

        @Test
        void semInserirNumeroDoProcesso() throws Exception {
            var processoInvalidoException =
                    new ProcessoInvalidoException("Numero do processo não informado!");
            doThrow(processoInvalidoException).when(beneficiarioService).preencherDados(dados);

            mvc.perform(post(API_URL + "/submit"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString("Numero do processo nÃ£o informado!")));
        }

        @Test
        void comSucesso() throws Exception {
            mvc.perform(post(API_URL + "/submit"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Campos preenchidos com sucesso!")));

            verify(beneficiarioService).preencherDados(dados);
        }
    }
}
