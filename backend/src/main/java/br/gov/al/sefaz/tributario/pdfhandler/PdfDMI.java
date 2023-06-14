package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.exception.PdfInvalidoException;
import br.gov.al.sefaz.tributario.pdfhandler.util.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PdfDMI extends PDF {
    private Area tabelaEsquerda;
    private Area tabelaDireita;

    protected PdfDMI(File file) {
        super(file);
        try {
            encontrarTabelaEsquerda();
            encontrarTabelaDireita();
        } catch (Exception e) {
            throw new PdfInvalidoException(Tipo.DMI, e);
        }
    }

    public Map<String, String> getTabela() {
        Map<String, String> dadosTabelaDireita = extrairTabelaDireita();
        Map<String, String> dadosTabelaEsquerda = extrairTabelaEsquerda();

        dadosTabelaDireita.putAll(dadosTabelaEsquerda);

        return dadosTabelaDireita;
    }

    private Map<String, String> extrairTabelaDireita() {
        BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
        Table table = bea.extract(pagina.getArea(tabelaDireita).getPage()).get(0);
        return converterTable(table);
    }

    private Map<String, String> extrairTabelaEsquerda() {
        BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
        Pagina paginaArea = pagina.getArea(tabelaEsquerda);
        Table table = bea.extract(paginaArea.getPage(), paginaArea.detectTable()).get(0);

        return getDadosTabelaEsquerda(table);
    }

    private static Map<String, String> getDadosTabelaEsquerda(Table table) {
        Iterator<String> iter = table.getRows().stream()
                .map(getTexto(table.getColCount() - 1, "[.%]"))
                .map(String::toLowerCase)
                .iterator();

        String cambio = encontrarValor("câmbio", iter);
        String aliquota = encontrarValor("interestadual", iter);

        Map<String, String> dados = new HashMap<>();
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

    @Override protected Map<String, String> criarMapID() {
        Map<String, String> map = new HashMap<>();

        map.put("impostodeimportação" , "valImpostoImportacao");
        map.put(  "impostoimportação" , "valImpostoImportacao");
        map.put(                "ipi" , "valIPI");
        map.put(                "pis" , "valPIS");
        map.put(             "cofins" , "valCOFINS");
        map.put(       "taxas/multas" , "valOutrasTaxas");
        map.put(        "icmsp/saída" , "valTotNotaFiscalSaida");

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

    private void encontrarTabelaEsquerda() throws IOException {
        try (PDDocument document = PDDocument.load(this.path.toFile())) {
            StringSearcher searcher = new StringSearcher();

            CharPosition topPos = searcher.findPositions(document, "OUTRAS INFORMAÇÕES").get(0);
            CharPosition bottomPos = searcher.findPositions(document, "Enquadramentos Legais").get(0);
            CharPosition rightPos = searcher.findPositions(document, "Valor da Importação").get(0);

            float bottom = bottomPos.getY() - bottomPos.getHeight() * 2;
            float right = rightPos.getX() - rightPos.getCharWidth();
            if (rightPos.getCharacter() == 'v') // se a numeracao do campo nao estiver presente
                right -= rightPos.getCharWidth() * 2;

            float x = 0;
            float y = topPos.getY() - topPos.getHeight() * 2;

            tabelaEsquerda = Area.withPosition(x, y).withWidth(right - x).andHeight(bottom - y);
        }
    }

    private void encontrarTabelaDireita() throws IOException {
        try (PDDocument document = PDDocument.load(this.path.toFile())) {
            StringSearcher searcher = new StringSearcher();

            CharPosition topPos = searcher.findPositions(document, "BASE DE CÁLCULO").get(0);
            List<CharPosition> result = searcher.findPositions(document, "R$");
            CharPosition bottomPos = result.get(result.size() - 1);
            CharPosition leftPos = searcher.findPositions(document, "Valor da Importação").get(0);

            float x = leftPos.getX() - leftPos.getCharWidth();
            if (leftPos.getCharacter() == 'v')
                x -= leftPos.getCharWidth();

            float y = topPos.getY() - topPos.getHeight() * 2;

            float bottom = bottomPos.getY() + bottomPos.getHeight();

            float right = pagina.width();

            tabelaDireita = Area.withPosition(x, y).withWidth(right - x).andHeight(bottom - y);
        }
    }
}
