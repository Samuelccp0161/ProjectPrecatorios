package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.pdfhandler.exception.PdfInvalidoException;
import br.gov.al.sefaz.tributario.pdfhandler.util.Pagina;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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

    PDF(File file) {
        try {
            this.pagina = Pagina.umDoArquivo(file);
            this.path = file.toPath();
        } catch (IOException e) {
            throw new PdfInvalidoException("Arquivo inválido!", e);
        }
    }

    public static PDF dmi(File file) throws IOException {
        return new PdfDMI(file);
    }

    public static PDF di(File file) throws IOException {
        return new PdfDI(file);
    }

    public PDF createDi(File file) throws IOException {
        return new PdfDI(file);
    }

    public static PDF from(File file, Tipo tipo) throws IOException{
        switch (tipo) {
            case DI:  return di(file);
            case DMI: return dmi(file);
        }

        throw new PdfInvalidoException("Tipo de pdf invalido!");
    }

    protected Map<String, String> converterTable(Table table) {
        Map<String, String> mapCampoId = criarMapID();
        Map<String, String> mapIdValor = tabelaDefault();

        Iterator<String> campos  = getCamposFrom(table);
        Iterator<String> valores = getValoresFrom(table);

        while (campos.hasNext() && valores.hasNext()) {
            String campo = campos.next();
            String valor = valores.next();
            if (valor.isEmpty()) continue;

            for (String key : mapCampoId.keySet())
                if (campo.contains(key)) {
                    String id = mapCampoId.remove(key);
                    mapIdValor.put(id, valor);
                    break;
                }
        }
        return mapIdValor;
    }

    private static Iterator<String> getValoresFrom(Table table) {
        return table.getRows().stream()
                .map(getTexto(table.getColCount() - 1, "[. -]"))
                .iterator();
    }

    private static Iterator<String> getCamposFrom(Table table) {
        return table.getRows().stream()
                .map(getCampo(table.getColCount(), "[. ]"))
                .map(String::toLowerCase)
                .iterator();
    }

    protected static Function<List<RectangularTextContainer>, String>
        getCampo(int nColumns, String removePattern)
    {
            return r -> {
                StringBuilder rowText = new StringBuilder();
                for (int i = 0; i < nColumns; i++)
                    rowText.append(r.get(i).getText())
                            .append(" ");
                return stripAndRemove(rowText.toString(), removePattern);
            };
    }

    protected static Function<List<RectangularTextContainer>, String> getTexto(int col, String removePattern) {
        return r ->  stripAndRemove(r.get(col).getText(), removePattern);
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

    abstract protected Map<String, String> tabelaDefault();
}
