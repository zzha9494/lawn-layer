package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

public class Tile {

    protected int x;
    protected int y;
    protected PImage sprite;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean checkCling(Tile t) {
        if (this.x == t.x && (this.y == t.y + 20 || this.y + 20 == t.y))
            return true;
        return this.y == t.y && (this.x == t.x + 20 || this.x + 20 == t.x);
    }

    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

}
