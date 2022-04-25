package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

public class Enemy extends Character{
    public Direction diagonal;

    public Enemy(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.diagonal = this.initialDiagonal();
    }

    public void randomSpawn(App app) {
        while(true) {
            int x = (int)(Math.random()*64);
            int y = (int)(Math.random()*32);

            this.x = x * 20;
            this.y = y * 20 + 80;

            if(this.collideCement(app) == null)
                return;
        }
    }

    public Cement collideCement (App app) {
        for (Cement cement: app.cementTiles)
            if (this.checkCollide(cement))
                return cement;
        return null;
    }

//    public void setSprite(PImage sprite) {
//        this.sprite = sprite;
//    }

    public void tick() {
        if (this.diagonal == Direction.TopLeft) {
            this.x -= 2;
            this.y -= 2;
        }
        if (this.diagonal == Direction.TopRight) {
            this.x += 2;
            this.y -= 2;
        }
        if (this.diagonal == Direction.BottomLeft) {
            this.x -= 2;
            this.y += 2;
        }
        if (this.diagonal == Direction.BottomRight) {
            this.x += 2;
            this.y += 2;
        }

    }

    public Direction initialDiagonal() {
        int i = (int)(Math.random()*4);
        if (i == 0)
            return Direction.TopLeft;
        if (i == 1)
            return Direction.TopRight;
        if (i == 2)
            return Direction.BottomLeft;
        return Direction.BottomRight;
    }

    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

}
