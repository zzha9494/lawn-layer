package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

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

    public abstract void tick();

    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

}
