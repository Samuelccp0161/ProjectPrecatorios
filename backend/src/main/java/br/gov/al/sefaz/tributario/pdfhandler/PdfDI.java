package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.exception.PdfInvalidoException;
import br.gov.al.sefaz.tributario.pdfhandler.util.Area;
import br.gov.al.sefaz.tributario.pdfhandler.util.CharPosition;
import br.gov.al.sefaz.tributario.pdfhandler.util.StringSearcher;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//TODO: eliminar a duplicação com PdfDMI
public class PdfDI extends PDF{
    private final String numeroDi;
    private Area tabela;

    protected PdfDI(File file) {
        super(file);
        try {
            this.numeroDi = getNumeroDI();
            encontrarTabela();
        } catch (Exception e) {
            throw new PdfInvalidoException(Tipo.DI, e);
        }
    }

    private void encontrarTabela() throws IOException {
        try (PDDocument document = PDDocument.load(this.path.toFile())) {
            StringSearcher searcher = new StringSearcher();

            CharPosition topPos = searcher.search(document, "Valores").get(0);
            CharPosition bottomPos = searcher.search(document, "Tributos").get(0);

            float x = 0;
            float y = topPos.getY() - topPos.getHeight() * 2;
            float bottom = bottomPos.getY() + bottomPos.getHeight() - bottomPos.getHeight() * 2;
            float right = pagina.width();

            tabela = Area.withPosition(x, y).withWidth(right).andHeight(bottom - y);
        }
    }

    private Table extrairTable() {
        BasicExtractionAlgorithm ea = new BasicExtractionAlgorithm();
        return ea.extract(pagina.getArea(tabela).getPage()).get(0);
    }

    @Override public Map<String, String> getTabela() {
        Table table = extrairTable();
        return converterTable(table);
    }

    @Override protected Map<String, String> criarMapID() {
        Map<String, String> map = new HashMap<>();

        map.put("frete" , "valFrete");
        map.put("seguro" , "valSeguro");
        map.put("vmle" , "valVMLE");

        return map;
    }

    @Override protected Map<String, String> tabelaDefault() {
        Map<String, String> map = new HashMap<>();

        map.put("valSeguro", "0,00");
        map.put( "valFrete", "0,00");
        map.put(  "valVMLE", "0,00");

        map.put("numDI", numeroDi);
        map.put("dataDMI", dataAtual());

        return map;
    }
    public String getNumeroDI() throws IOException {
        String cabecalho = pagina.getCabecalho().toUpperCase();
        String linhaDeclaracao = getLinhaDeclaracao(cabecalho);

        String[] words = linhaDeclaracao.split(" ");

        if (words.length < 2) return "";
        return words[1];
    }

    private static String getLinhaDeclaracao(String cabecalho) {
        return cabecalho.lines()
                .filter(str -> str.contains("DECLARAÇÃO:"))
                .findFirst()
                .map(str -> str.replaceAll("[/-]", ""))
                .orElse("");
    }

    public String dataAtual(){
        LocalDate date = LocalDate.now();

        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("ddMMuuuu");

        return formatterData.format(date);
    }
}
