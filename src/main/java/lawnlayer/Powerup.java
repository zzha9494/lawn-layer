package lawnlayer;

import processing.core.PImage;

/**
 * This class is inherited from {@code Tile} class.
 * A powerup can randomly respawn and has a random type.
 * The randomness is arranged when initialization.
 *
 * @author Zijie Zhao
 * @version 1.0
 * @since 22/04/2022
 */
public class Powerup extends Tile implements Spawn {
    int type;

    /**
     * Class constructor.
     * Automatically call methods to allocate a random position and type.
     * (The number 2 in {@code randomInterval % 2;} can be set as a static final attribute if you like.)
     *
     * @param app the running app
     */
    public Powerup(App app) {
        super(0, 0);
        this.randomSpawn(app);
        this.type = app.randomInterval % 2; // modulo number of types
        this.sprite = this.chooseType(app);
    }

    /**
     * Randomly set position and check if this position is empty.
     * The boundary of the map has been excepted.
     *
     * @param app the running app
     */
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

    /**
     * Automatically called when initialization. Assign image according to its type.
     *
     * @param app the running app
     * @return the image of this powerup
     */
    public PImage chooseType(App app) {
        if (this.type == 0)
            return app.powerup_0;
        else
//        if (this.type == 1) // register new type here
            return app.powerup_1;
    }

    /**
     * Main event of handling the powerup.
     * <p>
     * Check if the player collected a powerup.
     * If the player is having a powerup already,
     * this will update the player's powerup to the new one.
     * <p>
     * Show it on the information board and reset timers.
     * <p>
     * If A power is enclosed by grasses, remove it.
     * Show it on the information board and reset timers.
     *
     * @param app the running app
     */
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

    /**
     * Implement effects of a powerup.
     *
     * @param app the running app
     */
    public void validPowerup(App app) {
        if (this.type == 0)
            for (Enemy enemy: app.enemies)
                enemy.isFrozen = true;
        else if(this.type == 1)
            app.propagationSpeed = 6;
    }

    /**
     * Terminate effects of a powerup.
     *
     * @param app
     */
    public void invalidPowerup(App app) {
        if (this.type == 0)
            for (Enemy enemy: app.enemies)
                enemy.isFrozen = false;
        else if(this.type == 1)
            app.propagationSpeed = 3;
    }

}
