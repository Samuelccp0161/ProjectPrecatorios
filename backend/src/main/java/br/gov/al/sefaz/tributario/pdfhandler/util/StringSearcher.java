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
    private PDDocument pdf;
    private String searchText;
    private final List<CharPosition> resultPositions = new ArrayList<>();
    private final List<String> resultLines = new ArrayList<>();

    public StringSearcher() throws IOException {
        setSortByPosition(true);
        setStartPage(0);
        setEndPage(1);
    }

    public StringSearcher(PDDocument document) throws IOException {
        this();
        this.pdf = document;
    }

    public List<CharPosition> findPositions(PDDocument document, String text) throws IOException {
        doSearch(document, text);
        return resultPositions;
    }

    public CharPosition findFirstPositionWith(String text) throws IOException {
        doSearch(pdf, text);
        return resultPositions.get(0);
    }

    public String findFirstLineWith(String text) throws IOException {
        doSearch(pdf, text);
        return resultLines.size() > 0 ? resultLines.get(0) : "";
    }

    public List<String> findLinesWith(String text) throws IOException {
        doSearch(pdf, text);
        return resultLines;
    }

    private void doSearch(PDDocument document, String text) throws IOException {
        resultLines.clear();
        resultPositions.clear();
        this.searchText = text.toLowerCase();
        writeText(document, new OutputStreamWriter(new ByteArrayOutputStream()));
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) {
        text = text.toLowerCase();

        if (text.contains(searchText)) {
            resultLines.add(text);
            TextPosition firstChar = textPositions.get(0);
            float x = firstChar.getX();
            float y = firstChar.getY();
            float width = firstChar.getWidth();
            float height = firstChar.getHeightDir();

            Area charArea = Area.withPosition(x, y).withWidth(width).andHeight(height);
            resultPositions.add(new CharPosition(charArea, text.charAt(0)));
        }
    }
}
