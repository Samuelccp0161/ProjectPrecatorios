package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.pdfhandler.util.Pagina;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
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
        return converterTable(table, 0, table.getColCount() - 1);
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
}