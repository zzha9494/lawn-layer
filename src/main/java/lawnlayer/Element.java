package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

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

interface Spawn {
    void randomSpawn(App app);
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

    public boolean checkCollide(Element t) {
        return Math.abs(this.x - t.x) < 20 && Math.abs(this.y - t.y) < 20;
    }

    public <T extends Element> boolean checkInRegion (ArrayList<T> region) {
        for (T t: region)
            if (this.checkCollide(t))
                return true;
        return false;
    }

    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

}
