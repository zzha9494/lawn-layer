package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

enum Direction {
    Stop,
    Up,
    Down,
    Left,
    Right,
    TopLeft,
    TopRight,
    BottomLeft,
    BottomRight
}

public class Element {
    protected int x;
    protected int y;
    protected PImage sprite;

    public Element(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public Element(int x, int y) {
        this(x, y, null);
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
