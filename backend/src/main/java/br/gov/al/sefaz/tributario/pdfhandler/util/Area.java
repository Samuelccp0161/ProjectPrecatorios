package br.gov.al.sefaz.tributario.pdfhandler.util;

public class Area {
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public Area(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static Builder withPosition(float x, float y) {
        return new Builder(x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public static class Builder {
        private float x;
        private float y;
        private float width;

        public Builder(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Builder withWidth(float width) {
            this.width = width;
            return this;
        }

        public Area andHeight(float height) {
            return new Area(x, y, width, height);
        }
    }
}
