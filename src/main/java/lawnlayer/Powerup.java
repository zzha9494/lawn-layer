package lawnlayer;

import processing.core.PImage;

public class Powerup extends Tile implements Spawn {
    int type;

    public Powerup(App app) {
        super(0, 0);
        this.randomSpawn(app);
        this.type = app.randomInterval % 2; // modulo number of types
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
        else
//        if (this.type == 1) // register new type here
            return app.powerup_1;
    }

    public void checkCollected(App app) {
        if (this.checkCollide(app.player)) {
            if (app.collectedPowerup != null)
                app.collectedPowerup.invalidPowerup(app);
            this.x = 20;
            this.y = 52;
            app.collectedPowerup = this;
            this.validPowerup(app);
            app.unCollectedPowerup = null;
            app.powerupSpawnTimer = 0;
            app.powerupDurationTimer = 0;
        }
        else if (this.checkInRegion(app.grasses)) {
            app.unCollectedPowerup = null;
            app.powerupSpawnTimer = 0;
        }
    }

    public void validPowerup(App app) {
        if (this.type == 0)
            for (Enemy enemy: app.enemies)
                enemy.isFrozen = true;
        else if(this.type == 1)
            app.propagationSpeed = 6;
    }

    public void invalidPowerup(App app) {
        if (this.type == 0)
            for (Enemy enemy: app.enemies)
                enemy.isFrozen = false;
        else if(this.type == 1)
            app.propagationSpeed = 3;
    }

}
