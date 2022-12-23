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
import java.util.*;
import java.util.stream.Collectors;

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
            float left;
            if (leftPos.getText().startsWith("v"))
                left = leftPos.getX() - leftPos.getCharWidth() * 2;
            else left = leftPos.getX() - leftPos.getCharWidth();

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
            float right;
            if (rightPos.getText().startsWith("v"))
                right = rightPos.getX() - rightPos.getCharWidth() * 3 ;
            else right = rightPos.getX() - rightPos.getCharWidth() ;

            return pagina.getArea(top, 0, bottom, right);
        }
    }

    public Map<String, String> getTabela() throws IOException {
        BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
        Table table = bea.extract(getAreaTabelaDireita().getPage()).get(0);

        Map<String, String> dados;
        dados = converterTable(table, getColCampo(table), table.getColCount() - 1);
            printTable(table);
        Pagina areaEsquerda = getAreaTabelaEsquerda();
        table = bea.extract(areaEsquerda.getPage(), areaEsquerda.detectTable()).get(0);
            printTable(table);

        Iterator<String> iter = table.getRows().stream()
                .map(getTexto(table.getColCount() - 1, "[.%]"))
                .map(String::toLowerCase)
                .iterator();

        String cambio = encontrarValor("câmbio", iter);
        System.out.println("cambio" + cambio);
        String aliquota = encontrarValor("interestadual", iter);
//        aliquota = StrUtil.lastWord(aliquota);

        if (!cambio.isEmpty()) dados.put("valMoedaEstrangeira", cambio);
        if (!aliquota.isEmpty()) dados.put("valAliquota", aliquota);

        return dados;
    }

    private String parseCambio(String cambioRow) {
        List<String> words = Arrays.stream(cambioRow.split(" "))
                .filter(s -> s.length() == 6).collect(Collectors.toList());
        int len = words.size();
        return (len > 0)? words.get(len-1) : "";
    }

    private static String encontrarValor(String keyword, Iterator<String> iterator) {
        String valor = "";

        while (iterator.hasNext()) {
            String row = iterator.next();
            System.out.println(row);
            if (row.contains(keyword)) {
                valor = StrUtil.lastWord(row);
                break;
            }
        }
        return valor;
    }

    private int getColCampo(Table tabela) {
        for (var row : tabela.getRows()) {
            for (int i = 0; i < row.size(); i++) {
                String text = row.get(i).getText().toLowerCase();

                if (text.contains("importação"))
                    return i;
            }
        }
        return -1;
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

    @Override public boolean isValido() {
        try {
            String cabecalho = pagina.getCabecalho().toUpperCase();
            return cabecalho.contains("DESEMBARAÇO DE MERCADORIAS IMPORTADAS");
        } catch (IOException e) {
            return false;
        }
    }
}
