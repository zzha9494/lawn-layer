package lawnlayer;

import processing.core.PImage;

public class Enemy extends Character{
    public Enemy(int x, int y) {
        super(x, y, null);
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public void tick() {

    }

}
