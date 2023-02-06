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
    private final List<CharPosition> searchResult = new ArrayList<>();

    public StringSearcher() throws IOException {
        setSortByPosition(true);
        setStartPage(0);
        setEndPage(1);
    }

    public List<CharPosition> search(PDDocument document, String text) throws IOException {
        searchResult.clear();
        this.searchText = text.toLowerCase();
        writeText(document, new OutputStreamWriter(new ByteArrayOutputStream()));
        return searchResult;
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) {
        text = text.toLowerCase();

        if (text.contains(searchText)) {
            TextPosition firstChar = textPositions.get(0);
            float x = firstChar.getX();
            float y = firstChar.getY();
            float width = firstChar.getWidth();
            float height = firstChar.getHeightDir();

            Area charArea = Area.withPosition(x, y).withWidth(width).andHeight(height);
            searchResult.add(new CharPosition(charArea, text.charAt(0)));
        }
    }
}
