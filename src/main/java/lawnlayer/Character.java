package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Character extends Element{

    public Character(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

    public boolean checkCollide(Element t) {
        return Math.abs(this.x - t.x) < 20 && Math.abs(this.y - t.y) < 20;
    }

    public boolean collideCement (App app) {
        for (Cement cement: app.cementTiles)
            if (this.checkCollide(cement))
                return true;
        return false;
    }

    public abstract void tick();

}
