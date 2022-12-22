package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.pdfhandler.util.Pagina;
import br.gov.al.sefaz.tributario.pdfhandler.util.Position;
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
    protected PdfDI(File file) throws IOException {
        super(file);
    }

    private Pagina getAreaTabela() throws IOException {
        try (PDDocument document = PDDocument.load(this.path.toFile())) {
            StringSearcher searcher = new StringSearcher();

            Position topPos = searcher.search(document, "Valores").get(0);
            Position bottomPos = searcher.search(document, "Tributos").get(0);

            float top = topPos.getY() - topPos.getHeight() * 2;
            float bottom = bottomPos.getY() + bottomPos.getHeight() - bottomPos.getHeight() * 2;

            return pagina.getArea(top, 0, bottom, pagina.width());
        }
    }

    private Table extrairTable(Pagina areaTabela) {
        BasicExtractionAlgorithm ea = new BasicExtractionAlgorithm();
        return ea.extract(areaTabela.getPage()).get(0);
    }

    @Override public Map<String, String> getTabela() throws IOException {
        Table table = extrairTable(getAreaTabela());
        return converterTable(table, 0, table.getColCount() - 1);
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

        map.put("numDI", getNumeroDI());
        map.put("dataDMI", dataAtual());

        return map;
    }

    @Override public boolean isValido() {
        try {
            String cabecalho = pagina.getCabecalho().toUpperCase();
            return cabecalho.contains("EXTRATO DA DECLARAÇÃO DE IMPORTAÇÃO");
        } catch (IOException e) {
            return false;
        }
    }
    public String getNumeroDI() {
        try {
            String cabecalho = pagina.getCabecalho().toUpperCase();

            Optional<String> linhaDeclaracao = cabecalho.lines()
                    .filter(str -> str.contains("DECLARAÇÃO:"))
                    .findFirst()
                    .map(str -> str.replaceAll("[/-]", ""));
            
            if (linhaDeclaracao.isEmpty()) return "";

            String[] words = linhaDeclaracao.get().split(" ");
            
            return Arrays.stream(words).skip(1).findFirst().orElse("");
        } catch (IOException e) {
            return "";
        }
    }
    public String dataAtual(){
        LocalDate date = LocalDate.now();

        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("ddMMuuuu");

        return formatterData.format(date);
    }
}
