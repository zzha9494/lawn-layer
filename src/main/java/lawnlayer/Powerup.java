package lawnlayer;

import processing.core.PImage;

public class Powerup extends Tile implements Spawn {
    int type;

    public Powerup(App app) {
        super(0, 0);
        this.randomSpawn(app);
        this.type = app.randomInterval % 2;
        this.sprite = this.chooseType(app);
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

    public PImage chooseType(App app) {
        if (this.type == 0)
            return app.powerup_0;
        if (this.type == 1)
            return app.powerup_1;
        return null;
    }

    public void checkCollected(App app) {
        if (this.checkCollide(app.player)) {
            app.collectedPowerup = this;
            app.unCollectedPowerup = null;
            app.powerupSpawnTimer = 0;
        }
    }

}
