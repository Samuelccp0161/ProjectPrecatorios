package br.gov.al.sefaz.precatorio.pdfhandler.util;

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

    public boolean contains(Area other) {
        return  other.getX() >= this.getX() &&
                other.getY() >= this.getY() &&
                other.getWidth() <= this.getWidth() &&
                other.getHeight() <= this.getHeight();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Area area = (Area) o;

        if (Float.compare(area.x, x) != 0) return false;
        if (Float.compare(area.y, y) != 0) return false;
        if (Float.compare(area.width, width) != 0) return false;
        return Float.compare(area.height, height) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != 0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != 0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (width != 0.0f ? Float.floatToIntBits(width) : 0);
        result = 31 * result + (height != 0.0f ? Float.floatToIntBits(height) : 0);
        return result;
    }

    public static class Builder {
        private final float x;
        private final float y;
        private float width;
        private float height;

        public Builder(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Builder withWidth(float width) {
            this.width = width;
            return this;
        }

        public Builder withHeight(float height) {
            this.height = height;
            return this;
        }

        public Area build() {
            return andHeight(this.height);
        }

        public Area andHeight(float height) {
            return new Area(x, y, width, height);
        }
    }
}
