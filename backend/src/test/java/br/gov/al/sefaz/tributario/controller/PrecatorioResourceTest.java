package br.gov.al.sefaz.tributario.controller;

import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.exception.handler.CustomExceptionHandler;
import br.gov.al.sefaz.tributario.services.PrecatorioService;
import br.gov.al.sefaz.tributario.vo.Login;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PrecatorioResourceTest {
    protected MockMvc mvc;

    @Mock protected PrecatorioService precatorioService;

    @InjectMocks private PrecatorioResource precatorioResource;

    private final ObjectWriter jsonWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @BeforeEach void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(precatorioResource)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    protected String converterParaJson(Object obj) {
        try {
            return jsonWriter.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Nested @DisplayName("Ao Logar")
    public class AoLogar {

        private final Login loginVO = new Login("Usuario", "Senha");
        private final String loginJson = converterParaJson(loginVO);

        @Test @DisplayName("com login valido")
        public void comLoginValido() throws Exception {

            var request = criarLoginRequest();

            mvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk());

            verify(precatorioService).logar(loginVO.getUsuario(), loginVO.getSenha());
        }

        @Test @DisplayName("com login invalido")
        public void comLoginInvalido() throws Exception {
            var loginException = new LoginException("Usuário ou senha inválidos!");
            doThrow(loginException).when(precatorioService).logar(loginVO.getUsuario(), loginVO.getSenha());

            var request = criarLoginRequest();

            mvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("UsuÃ¡rio ou senha invÃ¡lidos!")));

            verify(precatorioService).logar(loginVO.getUsuario(), loginVO.getSenha());
        }

        private MockHttpServletRequestBuilder criarLoginRequest() {
            return post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(loginJson);
        }
    }
}
