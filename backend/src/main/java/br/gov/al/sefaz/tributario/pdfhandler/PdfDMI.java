package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.pdfhandler.util.Pagina;
import br.gov.al.sefaz.tributario.pdfhandler.util.Position;
import br.gov.al.sefaz.tributario.pdfhandler.util.StrUtil;
import br.gov.al.sefaz.tributario.pdfhandler.util.StringSearcher;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PdfDMI extends PDF {
    protected PdfDMI(File file) throws IOException {
        super(file);
    }

    private Pagina getAreaTabelaDireita() throws IOException {
        try (PDDocument document = PDDocument.load(this.path.toFile())) {
            StringSearcher searcher = new StringSearcher();

            Position topPos = searcher.search(document, "BASE DE CÁLCULO").get(0);
            List<Position> result = searcher.search(document, "R$");
            Position bottomPos = result.get(result.size()-1);
            Position leftPos = searcher.search(document, "Valor da Importação").get(0);

            float top = topPos.getY() - topPos.getHeight() * 2;
            float bottom = bottomPos.getY() + bottomPos.getHeight();
            float left = leftPos.getX() - leftPos.getCharWidth() ;

            return pagina.getArea(top, left, bottom, pagina.width());
        }
    }

    private Pagina getAreaTabelaEsquerda() throws IOException {
        try (PDDocument document = PDDocument.load(this.path.toFile())) {
            StringSearcher searcher = new StringSearcher();

            Position topPos = searcher.search(document, "OUTRAS INFORMAÇÕES").get(0);
            Position bottomPos = searcher.search(document, "Enquadramentos Legais").get(0);
            Position rightPos = searcher.search(document, "Valor da Importação").get(0);

            float top = topPos.getY() - topPos.getHeight() * 2;
            float bottom = bottomPos.getY() - bottomPos.getHeight() * 2;
            float right = rightPos.getX() - rightPos.getCharWidth() * 2 ;

            return pagina.getArea(top, 0, bottom, right);
        }
    }

    public Map<String, String> getTabela() throws IOException {
        BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
        Table table = bea.extract(getAreaTabelaDireita().getPage()).get(0);

        Map<String, String> dados;
        dados = converterTable(table, getColCampo(table), table.getColCount() - 1);

        Pagina areaEsquerda = getAreaTabelaEsquerda();
        table = bea.extract(areaEsquerda.getPage(), areaEsquerda.detectTable()).get(0);

        Iterator<String> iter = table.getRows().stream()
                .map(getTexto(table.getColCount() - 1, "[.%]"))
                .map(String::toLowerCase)
                .iterator();

        String cambio = encontrarValor("câmbio", iter);
        String aliquota = encontrarValor("interestadual", iter);

        if (!cambio.isEmpty()) dados.put("valMoedaEstrangeira", cambio);
        if (!aliquota.isEmpty()) dados.put("valAliquota", aliquota);

        return dados;
    }

    private static String encontrarValor(String keyword, Iterator<String> iterator) {
        String valor = "";

        while (iterator.hasNext()) {
            String row = iterator.next();
            if (row.contains(keyword)) {
                valor = StrUtil.lastWord(row);
                break;
            }
        }
        return valor;
    }

    private int getColCampo(Table tabela) {
        var row = tabela.getRows().get(0);
        for (int i = 0; i < row.size(); i++) {
            String text = row.get(i).getText().toUpperCase();

            if (text.contains("BASE DE CÁLCULO"))
                return i;
        }

        return -1;
    }

    @Override protected Map<String, String> criarMapID() {
        Map<String, String> map = new HashMap<>();

        map.put("impostoimportação" , "valImpostoImportacao");
        map.put(              "ipi" , "valIPI");
        map.put(              "pis" , "valPIS");
        map.put(           "cofins" , "valCOFINS");
        map.put(     "taxas/multas" , "valOutrasTaxas");
        map.put(      "icmsp/saída" , "valTotNotaFiscalSaida");

        return map;
    }

    @Override protected Map<String, String> tabelaDefault() {
        Map<String, String> map = new HashMap<>();

        map.put("valTotNotaFiscalSaida", "0,00");
        map.put( "valImpostoImportacao", "0,00");
        map.put(  "valMoedaEstrangeira", "0,00");
        map.put(       "valOutrasTaxas", "0,00");
        map.put(         "valCapatazia", "0,00");
        map.put(          "valAliquota", "4"   );
        map.put(            "valCOFINS", "0,00");
        map.put(               "valIPI", "0,00");
        map.put(               "valPIS", "0,00");

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
