package br.gov.al.sefaz.tributario.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TributarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    //Todo: concertar os testes das responses do login. o content não esta em utf-8
    @Test
    public void deveriaEfetuarLoginComSucesso() throws Exception {
        String params = getParamsLoginValido();

        this.mockMvc.perform(post("/api/login" + params).characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("UsuÃ¡rio logado com sucesso!")));
    }

    @Test
    public void deveriaEfetuarLoginComFalha() throws Exception {
        String params = getParamsLoginInvalido();

        this.mockMvc.perform(post("/api/login" + params).characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("UsuÃ¡rio ou senha invÃ¡lidos!")));
    }

    private static String getParamsLoginValido() {
        return "?usuario=sdcabral&senha=Samuka0810";
    }
    private static String getParamsLoginInvalido() {
        return "?usuario=abc&senha=abc";
    }
}