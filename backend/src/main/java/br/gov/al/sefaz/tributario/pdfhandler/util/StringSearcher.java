package br.gov.al.sefaz.tributario.pdfhandler.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class StringSearcher extends PDFTextStripper {
    private String searchText;
    private final List<Position> searchResult = new ArrayList<>();

    public StringSearcher() throws IOException {
        setSortByPosition(true);
        setStartPage(0);
        setEndPage(1);
    }

    public List<Position> search(PDDocument document, String text) throws IOException {
        searchResult.clear();
        this.searchText = text.toLowerCase();
        writeText(document, new OutputStreamWriter(new ByteArrayOutputStream()));
        return searchResult;
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) {
        if (text.toLowerCase().contains(searchText)) {
            TextPosition position = textPositions.get(0);
            float x = position.getX();
            float y = position.getY();
            float height = position.getHeightDir();
            float width = position.getWidth();
            searchResult.add(new Position(x, y, height, width, text));
        }
    }
}
