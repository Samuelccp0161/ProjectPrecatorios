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
    private Pagina areaDireita;
    private Pagina areaEsquerda;

    protected PdfDMI(File file) throws IOException {
        super(file);
    }

    private Pagina getAreaDireita() throws IOException {
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

    private Pagina getAreaEsquerda() throws IOException {
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
        this.areaDireita = getAreaDireita();
        this.areaEsquerda = getAreaEsquerda();

        Map<String, String> dados = extrairDados();
        dados.put("valCapatazia", "0,00");
        return dados;
    }

    private Map<String, String> extrairDados() {
        BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
//        Table table = bea.extract(areaDireita.getPage(), areaDireita.detectTable()).get(0);
        Table table = bea.extract(areaDireita.getPage()).get(0);

        int rowSize = table.getColCount();
        Map<String, String> dados =
                converterTable(table, getColCampo(table), rowSize - 1);

        table = bea.extract(areaEsquerda.getPage(), areaEsquerda.detectTable()).get(0);
        Iterator<String> iter = table.getRows().stream()
                .map(getTexto(table.getColCount() - 1)).iterator();

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

    private int getColCampo(Table tabela) {
        var row = tabela.getRows().get(0);
        for (int i = 0; i < row.size(); i++)
            if (row.get(i).getText().contains("BASE DE CÁLCULO"))
                return i;

        return -1;
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
