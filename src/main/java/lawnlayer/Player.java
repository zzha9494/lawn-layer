package lawnlayer;

import processing.core.PImage;

import java.util.ArrayList;

/**
 * This class is inherited from {@code Character} class.
 * A player.
 *
 * @author Zijie Zhao
 * @version 1.0
 * @since 22/04/2022
 */
public class Player extends Character {
    public int speed;
    public int lives;
    public boolean alive;

    public boolean centerCement;
    public boolean centerGrass;
    public boolean hitCement;

    public boolean leftMoving;
    public boolean rightMoving;
    public boolean upMoving;
    public boolean downMoving;
    public Direction leftRightDirection;
    public Direction upDownDirection;
    public Direction slideDirection;
    public Direction turnDirection;

    public ArrayList<Tile> floodArea;

    /**
     * Class constructor.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param sprite the image
     */
    public Player(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.alive = true;
        this.speed = 2;
        this.leftRightDirection = Direction.Stop;
        this.upDownDirection = Direction.Stop;
        this.floodArea = new ArrayList<Tile>();
    }

    /**
     * Set the lives of a player
     *
     * @param lives the lives
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Update some flags related to the player's position.
     *
     * @param app the running app
     */
    public void updatePositionFlag(App app) {
        this.hitCement = this.checkInRegion(app.cementTiles);
        this.centerCement = this.existsTile(this.x, this.y, app.cementTiles);
        this.centerGrass = this.existsTile(this.x, this.y, app.grasses);
    }

    /**
     * The behavior of moving on cement.
     * The position is constrained by snap grid.
     */
    public void normalMoving() {
        if(this.centerCement) {
            this.slideDirection = Direction.Stop;
            this.turnDirection = Direction.Stop;
        }

        if(this.leftRightDirection == Direction.Left && this.y % 20 ==0 && this.x > 0) {
            if (this.centerCement)
                this.slideDirection = Direction.Left;
            this.x -= this.speed;
            if (this.x % 20 ==0 && !this.leftMoving)
                this.leftRightDirection = Direction.Stop;
        }

        else if(this.leftRightDirection == Direction.Right  && this.y % 20 ==0 && this.x < 1260) {
            if (this.centerCement)
                this.slideDirection = Direction.Right;
            this.x += this.speed;
            if (this.x % 20 ==0 && !this.rightMoving)
                this.leftRightDirection = Direction.Stop;
        }

        else if(this.upDownDirection == Direction.Up && this.x % 20 ==0 && this.y > 80) {
            if (this.centerCement)
                this.slideDirection = Direction.Up;
            this.y -= this.speed;
            if (this.y % 20 ==0 && !this.upMoving)
                this.upDownDirection = Direction.Stop;
        }

        else if(this.upDownDirection == Direction.Down && this.x % 20 ==0 && this.y < 700) {
            if (this.centerCement)
               this.slideDirection = Direction.Down;
            this.y += this.speed;
            if (this.y % 20 ==0 && !this.downMoving) {
                this.upDownDirection = Direction.Stop;
            }
        }
    }

    /**
     * The behavior of moving on soil or grass.
     */
    public void soilMoving() {
        if(!this.hitCement) {
            this.leftRightDirection = Direction.Stop;
            this.upDownDirection = Direction.Stop;
        }

        if (this.x % 20 == 0 && this.y % 20 == 0)
            this.slideTurnDirection();

        if(this.slideDirection == Direction.Left) {
            this.x -= this.speed;
            this.leftRightDirection = Direction.Left;
        }

        if(this.slideDirection == Direction.Right) {
            this.x += this.speed;
            this.leftRightDirection = Direction.Right;
        }

        if(this.slideDirection == Direction.Up) {
            this.y -= this.speed;
            this.upDownDirection = Direction.Up;
        }

        if(this.slideDirection == Direction.Down) {
            this.y += this.speed;
            this.upDownDirection = Direction.Down;
        }
    }

    /**
     * Turn direction when sliding.
     */
    public void slideTurnDirection() {
        if (this.turnDirection == Direction.Left) {
            this.slideDirection = Direction.Left;
            this.turnDirection = Direction.Stop;
        }
        if (this.turnDirection == Direction.Right) {
            this.slideDirection = Direction.Right;
            this.turnDirection = Direction.Stop;
        }
        if (this.turnDirection == Direction.Up) {
            this.slideDirection = Direction.Up;
            this.turnDirection = Direction.Stop;
        }
        if (this.turnDirection == Direction.Down) {
            this.slideDirection = Direction.Down;
            this.turnDirection = Direction.Stop;
        }
    }


    /**
     * Create paths when moving
     * and automatically call {@code createGrass(app)} when reached a cement or grass.
     *
     * @param app the running app
     */
    public void createPath(App app) {
        if ((this.centerCement || this.centerGrass) && app.paths.size() != 0) {
            this.createGrass(app);
            app.paths.clear();
            return;
        }

        if (this.x % 20 == 0 && this.y % 20 == 0) {
            if(this.existsTile(this.x, this.y, app.cementTiles))
                return;
            if(this.existsTile(this.x, this.y, app.grasses))
                return;
            app.paths.add(new Path(this.x, this.y, app.green));
        }
    }

    /**
     * Turn paths to grasses
     * and automatically check an enclosed region.
     *
     * @param app the running app
     */
    public void createGrass(App app) {
        for (Path path: app.paths) {
            Grass grass = new Grass(path.x, path.y, app.grass);
            app.grasses.add(grass);
        }

        for (Path path: app.paths)
            this.captureTerritory(app, path);
    }


    /**
     * Check the nearby tiles.
     *
     * @param app the running app
     * @param p a path that is an origin of "flood"
     */
    public void captureTerritory(App app, Path p) {
        boolean encloseSuccess = false;
        if(isPlainSoil(app, p.x-20, p.y)) {
            this.floodArea.add(new Tile(p.x-20, p.y));
            this.floodFill(app, this.floodArea.get(0));
            encloseSuccess = this.encloseRegion(app);
        }
        if(isPlainSoil(app, p.x+20, p.y) && !encloseSuccess) {
            this.floodArea.add(new Tile(p.x+20, p.y));
            this.floodFill(app, this.floodArea.get(0));
            encloseSuccess = this.encloseRegion(app);
        }
        if(isPlainSoil(app, p.x, p.y+20) && !encloseSuccess) {
            this.floodArea.add(new Tile(p.x, p.y+20));
            this.floodFill(app, this.floodArea.get(0));
            encloseSuccess = this.encloseRegion(app);
        }
        if(isPlainSoil(app, p.x, p.y-20) && !encloseSuccess) {
            this.floodArea.add(new Tile(p.x, p.y-20));
            this.floodFill(app, this.floodArea.get(0));
            this.encloseRegion(app);
        }
    }

    /**
     * A helped method that implement the flood fill algorithm.
     *
     * @param app the running app
     * @param t the latest "flood", or the current node being visited.
     */
    public void floodFill(App app, Tile t) {
        if(isPlainSoil(app, t.x-20, t.y)) {
            Tile temp = new Tile(t.x-20, t.y);
            this.floodArea.add(temp);
            floodFill(app, temp);
        }
        if(isPlainSoil(app, t.x+20, t.y)) {
            Tile temp = new Tile(t.x+20, t.y);
            this.floodArea.add(temp);
            floodFill(app, temp);
        }
        if(isPlainSoil(app, t.x, t.y+20)) {
            Tile temp = new Tile(t.x, t.y+20);
            this.floodArea.add(temp);
            floodFill(app, temp);
        }
        if(isPlainSoil(app, t.x, t.y-20)) {
            Tile temp = new Tile(t.x, t.y-20);
            this.floodArea.add(temp);
            floodFill(app, temp);
        }
    }

    /**
     * Check a tile is empty or not.
     *
     * @param app the running app
     * @param x the x coordinate
     * @param y the y coordinate
     * @return {@code true} if the position is empty
     */
    public boolean isPlainSoil(App app, int x, int y) {
        if(this.existsTile(x, y, app.cementTiles))
            return false;
        if(this.existsTile(x, y, app.grasses))
            return false;
        if(this.existsTile(x, y, this.floodArea))
            return false;
        return true;
    }

    /**
     * Enclosed a region if there is no enemy within it.
     *
     * @param app the running app
     * @return {@code true} if a region is enclosed without an enemy
     */
    public boolean encloseRegion(App app) {
        for (Enemy enemy: app.enemies) {
            if (enemy.checkInRegion(this.floodArea)) {
                floodArea.clear();
                return false;
            }
        }
        for (Tile tile: floodArea)
            app.grasses.add(new Grass(tile.x, tile.y, app.grass));
        floodArea.clear();
        return true;
    }


    /**
     * Handle the event if the player lose a life.
     *
     * @param app the running app
     */
    public void checkLoseOneLife(App app) {
        if(this.checkInRegion(app.enemies) && !this.centerCement)
            this.alive = false;

        if(this.checkInRegion(app.currentRed))
            this.alive = false;

        for (int i = 0; i < app.paths.size()-1; i++) {
            if (this.checkCollide(app.paths.get(i)))
                this.alive = false;
        }

        if(!this.alive)
            this.playerRespawn(app);
    }

    /**
     * Respawn the player if not the last blood.
     *
     * @param app the running app
     */
    public void playerRespawn(App app) {
        this.lives --;
        if (this.lives == 0)
            return;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.moveOrigin(app);
    }

    /**
     * Move the player to its origin and clear all.
     *
     * @param app the running app
     */
    public void moveOrigin(App app) {
        this.x = 0;
        this.y = 80;
        this.alive = true;
        this.leftRightDirection = Direction.Stop;
        this.upDownDirection = Direction.Stop;
        this.slideDirection = Direction.Stop;
        this.turnDirection = Direction.Stop;

        app.paths.clear();
        if (app.collectedPowerup != null) {
            app.collectedPowerup.invalidPowerup(app);
            app.collectedPowerup = null;
        }
        app.unCollectedPowerup = null;
        app.powerupSpawnTimer = 0;
        app.powerupDurationTimer = 0;
        app.randomInterval = 0;
    }


    /**
     * Update the position per frame.
     */
    public void tick() {
        if(this.hitCement)
            this.normalMoving();
        else
            this.soilMoving();
    }
}
