package br.gov.al.sefaz.tributario.pdfhandler.util;

public class CharPosition {
    private final char character;
    private final Area area;

    public CharPosition(Area area, char character) {
        this.area = area;
        this.character = character;
    }

    public float getX() {
        return area.getX();
    }

    public float getY() {
        return area.getY();
    }

    public char getCharacter() {
        return character;
    }

    public float getHeight() {
        return area.getHeight();
    }

    public float getCharWidth() {
        return area.getWidth();
    }
}
