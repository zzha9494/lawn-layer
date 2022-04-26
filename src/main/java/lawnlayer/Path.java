package lawnlayer;

import processing.core.PImage;

public class Path extends Tile{
    public Boolean isRed;

    public Path(int x, int y, PImage sprite) {
        super(x, y);
        this.sprite = sprite;
        this.isRed = false;
    }

    public void turnRed(App app) {
        this.isRed = true;
        this.sprite = app.red;
    }

    public void propagateRed(App app) {

    }
}
