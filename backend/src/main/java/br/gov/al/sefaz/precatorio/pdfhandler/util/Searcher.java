package br.gov.al.sefaz.precatorio.pdfhandler.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Searcher extends PDFTextStripper {
    private final PDDocument pdf;
    private final List<Result> results = new ArrayList<>();
    private String searchText;

    public Searcher(PDDocument pdf) throws IOException {
        this.pdf = pdf;

        setSortByPosition(true);
        setStartPage(0);
        setEndPage(1);
    }

    public SearchResult doSearch(String text) throws IOException {
        this.searchText = text.toLowerCase();
        this.results.clear();
        writeText(pdf, new OutputStreamWriter(new ByteArrayOutputStream()));

        return new SearchResult(searchText, results);
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) {
        String line = text.toLowerCase();

        if (line.contains(searchText)) {
            TextPosition firstChar = textPositions.get(0);

            float x = firstChar.getX();
            float y = firstChar.getY();

            float width = textPositions.stream().map(TextPosition::getWidth).reduce(0F, Float::sum);
            float height = textPositions.stream().map(TextPosition::getHeight).reduce(0F, Float::max);

            Area area = Area.withPosition(x, y).withWidth(width).andHeight(height);
            results.add(new Result(line, area));
        }
    }

    static class Result {
        private final String line;
        private final Area area;
        Result(String line, Area area) {
            this.line = line;
            this.area = area;
        }

        public String getLine() {
            return line;
        }

        public Area getArea() {
            return area;
        }
    }
}
