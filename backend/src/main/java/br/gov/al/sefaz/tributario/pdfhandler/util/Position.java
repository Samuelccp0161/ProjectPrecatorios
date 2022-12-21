package br.gov.al.sefaz.tributario.pdfhandler.util;

public class Position {
    private final float x;
    private final float y;
    private final float height;
    private final float charWidth;
    private final String text;

    public Position(float x, float y, float height, float charWidth, String text) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.charWidth = charWidth;
        this.text = text;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getText() {
        return text;
    }

    @Override public String toString() {
        return String.format("(x=%-6s, y=%-6s) | %s", x, y, getText());
    }

    public float getHeight() {
        return height;
    }

    public float getCharWidth() {
        return charWidth;
    }
}
