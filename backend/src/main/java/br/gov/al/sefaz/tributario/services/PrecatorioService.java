package br.gov.al.sefaz.tributario.services;

import java.util.Map;

public interface PrecatorioService {
    void logar(String usuario, String senha);
    void irParaContaGrafica(String contaGrafica);
    void preencherCampos(Map<String, String> dados);
}
