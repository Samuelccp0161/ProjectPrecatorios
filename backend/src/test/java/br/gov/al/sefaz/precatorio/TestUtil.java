package br.gov.al.sefaz.precatorio;

import br.gov.al.sefaz.precatorio.selenium.Navegador;
import br.gov.al.sefaz.precatorio.service.impl.PrecatorioServiceImpl;

import java.nio.file.Paths;

public class TestUtil {
    private static final String URL = "https://precatorios.sefaz.al.gov.br/";
    private static final String PAGINA_LOCAL =
            "src/test/resources/precatorio-local/precatorio-nao-logado.html";

    public static String configurarAmbiente(PrecatorioServiceImpl precatorioService) {
        String ambienteDriver = System.getProperty("ambiente.webdriver", "host");

        switch (ambienteDriver.toLowerCase()) {
            case "local":   {
                String url = configurarAmbienteLocal();
                precatorioService.setUrl(url);
                return url;
            }
            case "docker":  return configurarAmbienteDocker();
            default: return configurarAmbienteHost();
        }
    }

    private static String configurarAmbienteHost() {
        Navegador.setAmbienteToHost();
        return URL;
    }
    private static String configurarAmbienteDocker() {
        Navegador.setAmbienteToDocker();
        return URL;
    }
    private static String configurarAmbienteLocal() {
        Navegador.setAmbienteToLocal();
        String ambienteSite = System.getProperty("ambiente.site", "web");

        if (ambienteSite.equalsIgnoreCase("web"))
            return URL;

        return "file://" + Paths.get(PAGINA_LOCAL).toAbsolutePath();
    }
}
