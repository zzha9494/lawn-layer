package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

public class Enemy extends Character{

    public Enemy(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

    public void randomSpawn(App app) {
        while(true) {
            int x = (int)(Math.random()*63);
            int y = (int)(Math.random()*31);

            this.x = x * 20;
            this.y = y * 20 + 80;

            if(!this.collideCement(app))
                return;
        }
    }

    public boolean collideCement (App app) {
        for (Cement cement: app.cementTiles)
            if (this.checkCollide(cement))
                return true;
        return false;
    }

//    public void setSprite(PImage sprite) {
//        this.sprite = sprite;
//    }

    public void tick() {


    }

    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

}
