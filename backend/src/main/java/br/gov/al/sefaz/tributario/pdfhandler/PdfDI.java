package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.pdfhandler.util.Pagina;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//TODO: eliminar a duplicação com PdfDMI
public class PdfDI extends PDF{
    private final Pagina areaTabela;
    protected PdfDI(File file) throws IOException {
        super(file);
        this.areaTabela = pagina.getAreaRelativa(39, 0, 68, 100);
    }

    private Table extrairTable() {
        BasicExtractionAlgorithm ea = new BasicExtractionAlgorithm();
        return ea.extract(areaTabela.getPage()).get(0);
    }

    @Override public Map<String, String> getTabela() {
        Table table = extrairTable();
        Map<String, String> tabela = converterTable(table, 0, table.getColCount() - 1);
        tabela.put("numDI", numeroDI());
        tabela.put("dataDMI", dataAtual());
        return tabela;
    }

    @Override protected Map<String, String> criarMapID() {
        Map<String, String> map = new HashMap<>();

        map.put("Frete" , "valFrete");
        map.put("Seguro" , "valSeguro");
        map.put("VMLE" , "valVMLE");

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
    public String numeroDI() {
        String cabecalho;
        try {
            cabecalho = pagina.getCabecalho().toUpperCase();
        } catch (IOException e) {
            return "";
        }
        String line = cabecalho.lines()
                .filter(str -> str.contains("DECLARAÇÃO:"))
                .findFirst().orElse("")
                .replaceAll("[/-]", "");

        return Arrays.stream(line.split(" ")).skip(1).findFirst().orElse("");
    }
    public String dataAtual(){
        LocalDate date = LocalDate.now();

        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("ddMMuuuu");
        String dataFormatada = formatterData.format(date);

        return dataFormatada;
    }

}
