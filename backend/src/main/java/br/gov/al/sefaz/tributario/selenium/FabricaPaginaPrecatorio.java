package br.gov.al.sefaz.tributario.selenium;

import org.springframework.stereotype.Component;

@Component
public class FabricaPaginaPrecatorio {
    private PaginaPrecatorio pagina;
    public PaginaPrecatorio getPagina() {
        if (pagina == null) {
            pagina = PaginaPrecatorio.criarComChrome();
            pagina.abrir();
        }

        return pagina;
    }
}
