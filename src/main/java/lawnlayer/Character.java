package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public abstract class Character extends Element{

    public Character(int x, int y, PImage sprite) {
        super(x, y, sprite);
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

    public <T extends Element> boolean existsTile(int x, int y, ArrayList<T> region) {
        for (T t: region)
            if (t.x == x && t.y == y)
                return true;
        return false;
    }

    public abstract void tick();

}
