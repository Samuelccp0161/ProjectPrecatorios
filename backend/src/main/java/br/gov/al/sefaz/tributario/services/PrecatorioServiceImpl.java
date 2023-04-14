package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.exception.ContaGraficaInvalidaException;
import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.selenium.FabricaPaginaPrecatorio;
import br.gov.al.sefaz.tributario.selenium.PaginaPrecatorio;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrecatorioServiceImpl implements PrecatorioService {

    private final FabricaPaginaPrecatorio fabricaPaginaPrecatorio;

    public PrecatorioServiceImpl(FabricaPaginaPrecatorio fabricaPaginaPrecatorio) {
        this.fabricaPaginaPrecatorio = fabricaPaginaPrecatorio;
    }


    public PaginaPrecatorio getPagina() {
        return fabricaPaginaPrecatorio.obterPagina();
    }

    public void logar(String usuario, String senha) {
        var pagina = getPagina();

        pagina.abrir();
        pagina.logar(usuario, senha);

        if (naoEstaLogado()) {
            throw new LoginException("Usuário ou senha inválidos!");
        }
    }

    protected boolean naoEstaLogado() {
        return !getPagina().isLogado();
    }

    public void irParaContaGrafica(String contaGrafica) {
        if (naoEstaLogado())
            throw new LoginException("Usuário não está logado!");

        getPagina().irParaContaGrafica(contaGrafica);

        if (naoEstaEmContaGrafica())
            throw new ContaGraficaInvalidaException("Conta gráfica inválida!");
    }

    protected boolean naoEstaEmContaGrafica() {
        return !getPagina().isEmContaGrafica();
    }

    public void preencherCampos(Map<String, String> dados) {
        if (naoEstaLogado())
            throw new LoginException("Usuário não está logado!");
        if (naoEstaEmContaGrafica())
            throw new ContaGraficaInvalidaException("Conta gráfica não informada!");

        var pagina = getPagina();

        for (Map.Entry<String,String> pair : dados.entrySet())
            pagina.preencherCampoPorID(pair.getKey(), pair.getValue());

        pagina.zerarPorcentagemICMSrecolher();
        pagina.clicarCampoNotaFiscal();
    }
}
