package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.pdfhandler.util.Pagina;
import br.gov.al.sefaz.tributario.pdfhandler.util.StrUtil;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PdfDMI extends PDF {
    private final Pagina areaDireita;
    private final Pagina areaEsquerda;

    protected PdfDMI(File file) throws IOException {
        super(file);
        this.areaDireita = pagina.getAreaRelativa(50, 48, 100, 100);
        this.areaEsquerda = pagina.getAreaRelativa(50, 0, 80, 47);
    }

    public Map<String, String> getTabela() {
        Map<String, String> dados = extrairDados();
        dados.put("valCapatazia", "0,00");
        return dados;
    }

    private Map<String, String> extrairDados() {
        BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
        Table table = bea.extract(areaDireita.getPage(), areaDireita.detectTable()).get(0);

        int rowSize = table.getColCount();
        Map<String, String> dados =
                converterTable(table, rowSize - 2, rowSize - 1);
        System.out.println("tabelaDireita");
        printTable(table);

        table = bea.extract(areaEsquerda.getPage(), areaEsquerda.detectTable()).get(0);
        Iterator<String> iter = table.getRows().stream()
                .map(getTexto(table.getColCount() - 1)).iterator();
        System.out.println("\n\ntabelaEsquerda");
        printTable(table);

        while (iter.hasNext()) {
            String row = iter.next();
            if (row.contains("Câmbio")) {
                String id = "valMoedaEstrangeira";
                String val = StrUtil.getFloats(row, ",").get(0);
                dados.put(id, val);
                break;
            }
        }
        while (iter.hasNext()) {
            String row = iter.next();
            if (row.contains("Interestadual")) {
                String id = "valAliquota";
                String val = StrUtil.getNumeros(row).get(0);
                dados.put(id, val);
                break;
            }
        }

        return dados;
    }

    @Override protected Map<String, String> criarMapID() {
        Map<String, String> map = new HashMap<>();

        map.put("ImpostoImportação" , "valImpostoImportacao");
        map.put(               "IPI" , "valIPI");
        map.put(               "PIS" , "valPIS");
        map.put(            "COFINS" , "valCOFINS");
        map.put(      "Taxas/Multas" , "valOutrasTaxas");
        map.put(     "ICMSp/Saída" , "valTotNotaFiscalSaida");

        return map;
    }

    @Override public boolean isValido() {
        try {
            String cabecalho = pagina.getCabecalho().toUpperCase();
            return cabecalho.contains("DESEMBARAÇO DE MERCADORIAS IMPORTADAS");
        } catch (IOException e) {
            return false;
        }
    }
}
