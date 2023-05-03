package br.gov.al.sefaz.tributario.services;

import java.util.Map;

public interface BeneficiarioService {
    void inserirNumeroProcesso(String numeroProcesso);

    void preencherDados(Map<String, String> dados);
}
