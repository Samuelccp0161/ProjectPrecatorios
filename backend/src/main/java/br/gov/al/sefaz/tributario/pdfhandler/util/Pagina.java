package br.gov.al.sefaz.tributario.pdfhandler.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.Rectangle;
import technology.tabula.detectors.SpreadsheetDetectionAlgorithm;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pagina {
    private final Page page;
    private final File file;

    private Pagina(Page page, File file) {
        this.page = page;
        this.file = file;
    }

    public static List<Pagina> doArquivo(File file) throws IOException {
        List<Pagina> pages = new ArrayList<>();
        try (PDDocument pdf = PDDocument.load(file)) {
            ObjectExtractor oe = new ObjectExtractor(pdf);
            oe.extract().forEachRemaining(pg -> pages.add(new Pagina(pg, file)));
        }
        return pages;

    }

    public static Pagina umDoArquivo(File file) throws IOException {
        try (PDDocument pdf = PDDocument.load(file)) {
            ObjectExtractor oe = new ObjectExtractor(pdf);
            return new Pagina(oe.extract(1), file);
        }
    }

    public Pagina getAreaRelativa(float top, float left, float bottom, float right) {
//        System.out.printf("w: %f; h; %f%n", page.width, page.height);

        Page newPage = page.getArea(
                page.height * top / 100,
                page.width * left / 100,
                page.height * bottom / 100,
                page.width * right / 100
        );
        return new Pagina(newPage, file);
    }

    public Page getPage() {
        return this.page;
    }

    public List<Float> detectTable() {
        List<Rectangle> rects = new SpreadsheetDetectionAlgorithm().detect(page);
        List<Float> verticalRulings = new ArrayList<>();

        for (Rectangle rect : rects) {
            verticalRulings.add(rect.getTop());
            verticalRulings.add(rect.getLeft());
            verticalRulings.add(rect.getBottom());
            verticalRulings.add(rect.getRight());
        }

        return verticalRulings;
    }

    public String getCabecalho() throws IOException {
        PDFTextStripperByArea stripper;
        try (PDDocument pdDoc = PDDocument.load(file)) {
            stripper = new PDFTextStripperByArea();

            Rectangle2D region = new Rectangle2D.Double(0, 0, page.width, page.height / 7);
            stripper.addRegion("header", region);
            stripper.extractRegions(pdDoc.getPage(0));
        }

        return stripper.getTextForRegion("header");
    }
}
