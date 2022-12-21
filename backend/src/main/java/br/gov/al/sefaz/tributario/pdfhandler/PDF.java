package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.pdfhandler.exception.PdfInvalidoException;
import br.gov.al.sefaz.tributario.pdfhandler.util.Pagina;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static br.gov.al.sefaz.tributario.pdfhandler.util.StrUtil.stripAndRemove;
import static br.gov.al.sefaz.tributario.pdfhandler.util.StrUtil.truncar;

public abstract class PDF {
    protected Path path;
    protected Pagina pagina;

    public enum Tipo {DI, DMI}

    PDF(File file) throws IOException {
        this.pagina = Pagina.umDoArquivo(file);
        this.path = file.toPath();
    }

    public static PDF dmi(File file) throws IOException {
        return new PdfDMI(file);
    }

    public static PDF di(File file) throws IOException {
        return new PdfDI(file);
    }

    public static PDF from(File file, Tipo tipo) throws IOException{
        switch (tipo) {
            case DI:  return di(file);
            case DMI: return dmi(file);
        }

        throw new PdfInvalidoException("Tipo de pdf invalido!");
    }

    protected Map<String, String> converterTable(Table table, int colunaCampo, int colunaValor) {
        Map<String, String> mapCampoId = criarMapID();
        Map<String, String> mapIdValor = new HashMap<>();

        Iterator<String> campos = table.getRows().stream().map(getTexto(colunaCampo)).iterator();
        Iterator<String> valores = table.getRows().stream().map(getTexto(colunaValor)).iterator();

        while (campos.hasNext() && valores.hasNext()) {
            String campo = campos.next();
            String valor = valores.next();

            valor = (valor.equals("")) ? "0,00" : valor;

            for (String key : mapCampoId.keySet())
                if (campo.contains(key)) {
                    String id = mapCampoId.remove(key);
                    mapIdValor.put(id, valor);
                    break;
                }
        }
        return mapIdValor;
    }

    protected static Function<List<RectangularTextContainer>, String> getTexto(int i) {
        return r -> stripAndRemove(r.get(i).getText(), "[. %-]");
    }

    @SuppressWarnings("unused")
    protected void printTable(Table tabela) {
        String cellFMT = " %-30s |";

        System.out.println("#----------------------#");
        for (var row : tabela.getRows()) {
            System.out.print("|");
            for (var cell : row)
                System.out.printf(cellFMT, truncar(cell.getText(), 30));
            System.out.println();
        }
        System.out.println("#----------------------#");
    }

    abstract public Map<String, String> getTabela() throws IOException;

    abstract protected Map<String, String> criarMapID();

    public abstract boolean isValido();
}
