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

public abstract class Character {
    protected int x;
    protected int y;
    protected PImage sprite;

    public Character(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public boolean checkCollide(Tile t) {
        return Math.abs(this.x - t.x) < 20 && Math.abs(this.y - t.y) < 20;
    }

    public boolean checkCollide(Character c) {
        return Math.abs(this.x - c.x) < 20 && Math.abs(this.y - c.y) < 20;
    }

    public boolean checkCling(Tile t) {
        if (this.x == t.x && (this.y == t.y + 20 || this.y + 20 == t.y))
            return true;
        return this.y == t.y && (this.x == t.x + 20 || this.x + 20 == t.x);
    }

    public Cement collideCement (App app) {
        for (Cement cement: app.cementTiles)
            if (this.checkCollide(cement))
                return cement;
        return null;
    }

    public abstract void tick();

    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

}
