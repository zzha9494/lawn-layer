package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Character {
    protected int x;
    protected int y;
    protected final PImage sprite;

    public Character(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public abstract void tick();

    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

}
