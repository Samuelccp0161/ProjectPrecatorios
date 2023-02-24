package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.selenium.PaginaPrecatorio;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrecatorioService {
    private PaginaPrecatorio pagina;

    public PaginaPrecatorio getPagina() {
        if (pagina == null) {
            pagina = PaginaPrecatorio.criar();
            pagina.abrir();
        }

        return pagina;
    }

    public void logar(String usuario, String senha) {
        var pagina = getPagina();
        pagina.abrir();
        pagina.logar(usuario, senha);
    }

    public boolean isLogado() {
        return getPagina().isLogado();
    }

    public void irParaContaGrafica(String contaGrafica) {
        if (isLogado())
            getPagina().irParaContaGrafica(contaGrafica);
    }

    public boolean isEmContaGrafica() {
        return getPagina().isEmContaGrafica();
    }

    public void preencherCampos(Map<String, String> dados) {
        for (Map.Entry<String,String> pair : dados.entrySet())
            pagina.preencherCampoPorID(pair.getKey(), pair.getValue());

        pagina.zerarPorcentagemICMSrecolher();
        pagina.clicarCampoNotaFiscal();
    }

    public void focarJanela() {
        pagina.minimizar();
        pagina.maximizar();
    }
}
