package br.gov.al.sefaz.tributario.selenium;

import org.springframework.stereotype.Component;

@Component
public class FabricaPaginaPrecatorio {
    private PaginaPrecatorio pagina;
    public PaginaPrecatorio obterPagina() {
        if (pagina == null) {
            pagina = new PaginaPrecatorio();
            pagina.abrir();
        }

        return pagina;
    }
}
