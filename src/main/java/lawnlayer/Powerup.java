package lawnlayer;

import processing.core.PImage;

public class Powerup extends Tile implements Spawn {
    int type;
    boolean visible;
    boolean valid;

    public Powerup(int x, int y) {
        super(x, y);
        this.visible = false;
        this.valid = false;
    }

    public void randomSpawn(App app) {
        while(true) {
            int x = (int)(Math.random()*62);
            int y = (int)(Math.random()*30);

            this.x = 20 + x * 20;
            this.y = 20 + y * 20 + 80;

            if(!this.checkInRegion(app.cementTiles) && !this.checkInRegion(app.grasses) && !this.checkInRegion(app.paths))
                return;
        }
    }
}
