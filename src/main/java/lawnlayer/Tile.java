package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

public class Tile {

    protected int x;
    protected int y;
    private PImage sprite;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

}
