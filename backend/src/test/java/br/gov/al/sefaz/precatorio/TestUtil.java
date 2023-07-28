package br.gov.al.sefaz.precatorio;

import br.gov.al.sefaz.precatorio.selenium.FabricaDriver;
import br.gov.al.sefaz.precatorio.service.impl.PrecatorioServiceImpl;

import java.nio.file.Paths;

public class TestUtil {
    private static final String URL = "https://precatorios.sefaz.al.gov.br/";
    private static final String PAGINA_LOCAL =
            "src/test/resources/precatorio-local/precatorio-nao-logado.html";

    public static String configurarAmbiente(PrecatorioServiceImpl precatorioService) {
        String ambienteDriver = System.getProperty("ambiente.webdriver", "docker");
        String ambienteSite = System.getProperty("ambiente.site", "web");

        if (ambienteDriver.equalsIgnoreCase("docker"))
            return URL;

        FabricaDriver.setLocal("src/test/resources/chromedriver");

        if (ambienteSite.equalsIgnoreCase("web"))
            return URL;

        String urlLocal = "file://" + Paths.get(PAGINA_LOCAL).toAbsolutePath();
        precatorioService.setUrl(urlLocal);

        return urlLocal;
    }


    public static void configurarAmbienteWebdriver() {
        String ambienteDriver = System.getProperty("ambiente.webdriver", "local");
        if (!ambienteDriver.equalsIgnoreCase("remoto")) {
            FabricaDriver.setLocal();
        }
    }

    public static String configurarAmbientePagina(PrecatorioServiceImpl precatorioService) {
        String url = TestUtil.URL;
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
