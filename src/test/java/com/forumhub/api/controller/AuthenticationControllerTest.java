package com.forumhub.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.forumhub.api.domain.usuario.DadosAutenticacao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAutenticacao> dadosAutenticacaoJson;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    void efetuarLogin_cenario1() throws Exception {
        var response = mvc
                .perform(post("/login"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas")
    void efetuarLogin_cenario2() throws Exception {
        var dadosAutenticacao = new DadosAutenticacao("admin", "123456");

        var response = mvc
                .perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dadosAutenticacaoJson.write(dadosAutenticacao).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());

        var jsonResponse = response.getContentAsString();
        assertThat(jsonResponse).contains("token");
    }
}
