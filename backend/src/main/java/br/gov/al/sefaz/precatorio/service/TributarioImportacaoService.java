package br.gov.al.sefaz.precatorio.service;

import java.util.Map;

public interface TributarioImportacaoService {
    void irParaContaGrafica(String contaGrafica);

    void preencherDados(Map<String , String> dados);
}
