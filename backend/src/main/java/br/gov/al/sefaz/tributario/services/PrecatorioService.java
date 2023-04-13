package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.exception.ContaGraficaInvalidaException;
import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.selenium.FabricaPaginaPrecatorio;
import br.gov.al.sefaz.tributario.selenium.PaginaPrecatorio;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrecatorioService {

    private final FabricaPaginaPrecatorio fabrica;

    public PrecatorioService(FabricaPaginaPrecatorio fabrica) {
        this.fabrica = fabrica;
    }

    public PaginaPrecatorio getPagina() {
        return fabrica.getPagina();
    }

    public void logar(String usuario, String senha) {
        var pagina = getPagina();

        pagina.abrir();
        pagina.logar(usuario, senha);

        if (!isLogado()) {
            throw new LoginException("Usuário ou senha inválidos!");
        }
    }

    public boolean isLogado() {
        return getPagina().isLogado();
    }

    public void irParaContaGrafica(String contaGrafica) {
        if (!isLogado())
            throw new LoginException("Usuário não está logado!");

        getPagina().irParaContaGrafica(contaGrafica);

        if (!isEmContaGrafica())
            throw new ContaGraficaInvalidaException("Conta gráfica inválida!");
    }

    public boolean isEmContaGrafica() {
        return getPagina().isEmContaGrafica();
    }

    public void preencherCampos(Map<String, String> dados) {
        if (!isLogado())
            throw new LoginException("Usuário não está logado!");
        if (!isEmContaGrafica())
            throw new ContaGraficaInvalidaException("Conta gráfica não informada!");

        var pagina = getPagina();

        for (Map.Entry<String,String> pair : dados.entrySet())
            pagina.preencherCampoPorID(pair.getKey(), pair.getValue());

        pagina.zerarPorcentagemICMSrecolher();
        pagina.clicarCampoNotaFiscal();
    }
}
