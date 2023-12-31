package br.gov.al.sefaz.precatorio.pdfhandler;

import br.gov.al.sefaz.precatorio.exception.PdfInvalidoException;
import br.gov.al.sefaz.precatorio.pdfhandler.util.Area;
import br.gov.al.sefaz.precatorio.pdfhandler.util.Searcher;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PdfDI {
    private final Path arquivo;
    private final String numeroDi;
    private final Area areaTabela;

    public PdfDI(Path path) {
        if (!path.toFile().exists())
            throw new PdfInvalidoException("Arquivo inexistente!");

        this.arquivo = path;

        try (PDDocument pdf = PDDocument.load(arquivo.toFile())) {
            this.numeroDi = extrairNumeroDi(pdf);
            this.areaTabela = encontrarTabela(pdf);
        } catch (Exception e) {
            throw new PdfInvalidoException("O Arquivo enviado não é um 'DI' válido", e);
        }
    }

    private static String extrairNumeroDi(PDDocument pdf) {
        try {
            Searcher searcher = new Searcher(pdf);
            String resultado = searcher.doSearch("Declaração").getFirstLine().orElseThrow();
            return resultado.replaceAll("\\D", "");
        } catch (Exception e) {
            throw new PdfInvalidoException("Formatação do Pdf inválida", e);
        }
    }

    private static Area encontrarTabela(PDDocument pdf) throws IOException {
        Searcher searcher = new Searcher(pdf);

        Area topLine = searcher.doSearch("Valores").getFirstArea();
        Area bottomLine = searcher.doSearch("Tributos").getFirstArea();

        float height = bottomLine.getY() - topLine.getY();
        float width = pdf.getPage(0).getMediaBox().getWidth();

        float x = 0F;
        float y = topLine.getY();

        return Area.withPosition(x, y)
                .withWidth(width)
                .withHeight(height)
                .build();
    }

    public Map<String, String> extrairDados() {
        Table tabela = extrairTabela();
//        printTable(tabela);
        return converterTable(tabela);
    }

    private Map<String, String> converterTable(Table tabela) {
        Iterator<String> campos = extrairColuna(tabela, 0);
        Iterator<String> valores = extrairColuna(tabela, 2);

        Map<String, String> dados = new HashMap<>() {{
            put("numDI", numeroDi);
            put("valFrete", "0,00");
            put("valSeguro", "0,00");
            put("valVMLE", "0,00");
        }};

        while (campos.hasNext() && valores.hasNext()) {
            String campo = campos.next();
            String valor = valores.next();

            if (!valor.matches("\\d+,\\d{2}")) continue;    //pular se valor nao eh monetario

            switch (campo) {
                case "frete":  dados.put("valFrete", valor);
                case "seguro": dados.put("valSeguro", valor);
                case "vmle":   dados.put("valVMLE", valor);
            }
        }

        return dados;
    }

    private Iterator<String> extrairColuna(Table tabela, int col) {
        return tabela.getRows().stream()
                .map(row -> row.get(col))
                .map(RectangularTextContainer::getText)
                .map(String::toLowerCase)
                .map(str -> str.replaceAll("[^\\w,]", ""))
                .map(String::strip)
                .iterator();
    }

    private Table extrairTabela() {
        try (PDDocument pdf = PDDocument.load(arquivo.toFile())) {
            ObjectExtractor oe = new ObjectExtractor(pdf);

            Page pagina = oe.extract(1).getArea(
                    areaTabela.getY(),
                    areaTabela.getX(),
                    areaTabela.getY() + areaTabela.getHeight(),
                    areaTabela.getX() + areaTabela.getWidth()
            );

            return new BasicExtractionAlgorithm().extract(pagina).get(0);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getNumeroDi() {
        return numeroDi;
    }

    protected Area getAreaTabela() {
        return areaTabela;
    }

    @SuppressWarnings("unused")
    protected void printTable(Table tabela) {
        String cellFMT = " %-30s |";

        System.out.println("#----------------------#");
        for (var row : tabela.getRows()) {
            System.out.print("|");
            for (var cell : row) {
                String result;

                String str = cell.getText();
                if (str.length() <= 30) {
                    result = str;
                } else {
                    result = str.substring(0, 30 - 2) + "..";
                }

                System.out.printf(cellFMT, result);
            }
            System.out.println();
        }
        System.out.println("#----------------------#");
    }
}
