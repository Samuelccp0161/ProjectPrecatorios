package br.gov.al.sefaz.tributario.services;

import java.util.Map;

public interface TributarioImportacaoService {
    void irParaContaGrafica(String contaGrafica);

    void preencherDados(Map<String , String> dados);
}
