package br.gov.al.sefaz.tributario;

import br.gov.al.sefaz.tributario.selenium.FabricaDriver;
import br.gov.al.sefaz.tributario.service.impl.PrecatorioServiceImpl;

import java.nio.file.Paths;

public class TestUtil {
    private static final String url = "https://precatorios.sefaz.al.gov.br/";


    public static void configurarAmbienteWebdriver() {
        String ambienteDriver = System.getProperty("ambiente.webdriver", "local");
        if (!ambienteDriver.equalsIgnoreCase("remoto")) {
            FabricaDriver.setLocal();
        }
    }

    public static String configurarAmbientePagina(PrecatorioServiceImpl precatorioService) {
        String url = TestUtil.url;
        String ambienteSite = System.getProperty("ambiente.site", "local");

        if (!ambienteSite.equalsIgnoreCase("remoto")) {
            var paginaLocal = Paths.get("src/test/resources/precatorio-local/precatorio-nao-logado.html")
                    .toAbsolutePath();
            url = "file://" + paginaLocal;
            precatorioService.setUrl(url);
        }

        return url;
    }
}
