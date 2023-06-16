package br.gov.al.sefaz.precatorio.pdfhandler;

import br.gov.al.sefaz.precatorio.exception.PdfInvalidoException;
import br.gov.al.sefaz.precatorio.pdfhandler.util.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.BasicExtractionAlgorithm;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class PdfDMI {
    private final Area areaTabelaEsquerda;
    private final Area areaTabelaDireita;
    private final Path arquivo;

    public PdfDMI(Path arquivo) {
        if (!arquivo.toFile().exists())
            throw new PdfInvalidoException("Arquivo inexistente!");

        this.arquivo = arquivo;

        try (PDDocument pdf = PDDocument.load(arquivo.toFile())){
            areaTabelaEsquerda = encontrarTabelaEsquerda(pdf);
            areaTabelaDireita = encontrarTabelaDireita(pdf);

        } catch (Exception e) {
            throw new PdfInvalidoException("O Arquivo enviado não é um 'DMI' válido", e);
        }

    }

    private static Area encontrarTabelaDireita(PDDocument pdf) throws IOException {
        Searcher searcher = new Searcher(pdf);

        Area topPos = searcher.doSearch("BASE DE CÁLCULO").getFirstArea();
        Area bottomPos = searcher.doSearch("R$").getLastArea();
        Area leftPos = searcher.doSearch("Valor da Importação").getFirstArea();

        float x = leftPos.getX() - 15f;
        float y = topPos.getY();

        float height = bottomPos.getY() - y;
        float width = pdf.getPage(0).getMediaBox().getWidth() - x;

        return Area.withPosition(x, y).withWidth(width).withHeight(height).build();
    }
    private static Area encontrarTabelaEsquerda(PDDocument pdf) throws IOException {
        Searcher searcher = new Searcher(pdf);

        Area topPos = searcher.doSearch("OUTRAS INFORMAÇÕES").getFirstArea();
        Area bottomPos = searcher.doSearch("Enquadramentos Legais").getFirstArea();
        Area rightPos = searcher.doSearch("Valor da Importação").getFirstArea();

        float x = 0;
        float y = topPos.getY();

        float width = rightPos.getX() - 15F;
        float height = bottomPos.getY() - y;

        return Area.withPosition(x, y).withWidth(width).andHeight(height);
    }

    protected Area getAreaTabelaEsquerda() {
        return areaTabelaEsquerda;
    }
    protected Area getAreaTabelaDireita() {
        return areaTabelaDireita;
    }

    public Map<String, String> extrairDados() {
        Map<String, String> dados = new HashMap<>() {{
            put("valTotNotaFiscalSaida", "0,00");
            put("valImpostoImportacao", "0,00");
            put("valMoedaEstrangeira", "0,00");
            put("valOutrasTaxas", "0,00");
            put("valCapatazia", "0,00");
            put("valAliquota", "4");
            put("valCOFINS", "0,00");
            put("valIPI", "0,00");
            put("valPIS", "0,00");
        }};

        dados.putAll(extrairDadosTabelaEsquerda());
        dados.putAll(extrairDadosTabelaDireita());

        return dados;
    }

    private Table extrairTabelaEsquerda() {
        try (PDDocument pdf = PDDocument.load(arquivo.toFile())){

            Page pagina = cortarPagina(pdf, areaTabelaEsquerda);

            Area primeiraLinha = new Searcher(pdf).doSearch("câmbio").getFirstArea();
            List<Float> rulings = new ArrayList<>() {{
                add(primeiraLinha.getX() + primeiraLinha.getWidth());
            }};

            return new BasicExtractionAlgorithm().extract(pagina, rulings).get(0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Table extrairTabelaDireita() {
        try (PDDocument pdf = PDDocument.load(arquivo.toFile())){

            Page pagina = cortarPagina(pdf, areaTabelaDireita);

            Area primeiraLinha = new Searcher(pdf)
                    .doSearch("r$")
                    .getFirstAreaInside(areaTabelaDireita);
            List<Float> rulings = new ArrayList<>() {{
                add(primeiraLinha.getX() + primeiraLinha.getWidth());
            }};

            return new BasicExtractionAlgorithm().extract(pagina, rulings).get(0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> extrairDadosTabelaDireita() {
        Table tabela = extrairTabelaDireita();

        List<Row> rows = extrairRowsDaTabela(tabela);

        Map<String, String> dados = new HashMap<>();

        for (var row : rows) {
            String campo = row.campo;
            String valor = row.valor.replaceAll("[^\\d,]", "");

            if (valor.isBlank()) continue;

            if (campo.matches(".*imposto.+importação.*"))
                dados.put("valImpostoImportacao", valor);
            else if (campo.matches(".*i.?p.?i.*"))
                dados.put("valIPI", valor);
            else if (campo.matches(".*p.?i.?s.*"))
                dados.put("valPIS", valor);
            else if (campo.matches(".*cofins.*"))
                dados.put("valCOFINS", valor);
            else if (campo.matches(".*taxas.multas.*"))
                dados.put("valOutrasTaxas", valor);
            else if (campo.matches(".*icms.*saída.*"))
                dados.put("valTotNotaFiscalSaida", valor);
        }

        return dados;
    }
    private Map<String, String> extrairDadosTabelaEsquerda() {
        Table tabela = extrairTabelaEsquerda();

        List<Row> rows = extrairRowsDaTabela(tabela);

        Map<String, String> dados = new HashMap<>();

        for (var row : rows) {
            String campo = row.campo;
            String value = row.valor.replaceAll("[^\\d,]", "");

            if (value.isBlank()) continue;

            if (campo.contains("interestadual"))
                dados.put("valAliquota", value);
            else if (campo.contains("câmbio"))
                dados.put("valMoedaEstrangeira", value);
        }

        return dados;
    }

    private Page cortarPagina(PDDocument pdf, Area areaTabela) {
        ObjectExtractor oe = new ObjectExtractor(pdf);

        return oe.extract(1).getArea(
                areaTabela.getY(),
                areaTabela.getX(),
                areaTabela.getY() + areaTabela.getHeight(),
                areaTabela.getX() + areaTabela.getWidth()
        );
    }

    private static List<Row> extrairRowsDaTabela(Table tabela) {
        return tabela.getRows().stream()
                .filter(row -> row.size() >= 2)
                .map(Row::fromList)
                .collect(Collectors.toList());
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

    static class Row {

        private final String campo;
        private final String valor;

        Row(String campo, String valor) {
            this.campo = campo;
            this.valor = valor;
        }

        static Row fromList(List<? extends HasText> list) {
            return new Row(
                    list.get(0).getText().toLowerCase(),
                    list.get(1).getText().toLowerCase()
            );
        }
    }
}
