package lawnlayer;

import processing.core.PImage;

import java.util.ArrayList;

/**
 * This class is inherited from {@code Character} class.
 * <p>
 * A general enemy.
 *
 * @author Zijie Zhao
 * @version 1.0
 * @since 22/04/2022
 */
public class Enemy extends Character implements Spawn {
    public int speed;
    public int type;
    public boolean isFrozen;
    public Direction diagonal;

    /**
     * @param x the x coordinate
     * @param y the y coordinate
     * @param sprite the image
     */
    public Enemy(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.diagonal = this.initialDiagonal();
        speed = 2;
        type = 0;
    }

    /**
     * Get a random direction when an enemy is initialized.
     *
     * @return The direction
     */
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

            if(!this.checkInRegion(app.cementTiles))
                return;
        }
    }

    /**
     * Handle the reflection of an enemy when hitting another tile.
     * <p>
     * When hit a right angle, it will turn back.
     *
     * @param app the running app
     */
    public void reflectDirection(App app) {
        ArrayList<Tile> clingTiles = this.getClingTiles(app);
        if (clingTiles.size() == 0 && (this.checkInRegion(app.grasses) || this.checkInRegion(app.cementTiles) || this.checkInRegion(app.paths))) {
            this.turnBack();
            return;
        }

        if (clingTiles.size() == 1) {
            Tile clingWhat = clingTiles.get(0);
            if (this.x == clingWhat.x && this.y == clingWhat.y + 20)
                if (this.diagonal == Direction.TopRight)
                    this.diagonal = Direction.BottomRight;
                else
                    this.diagonal = Direction.BottomLeft;
            if (this.x == clingWhat.x && this.y + 20 == clingWhat.y)
                if (this.diagonal == Direction.BottomRight)
                    this.diagonal = Direction.TopRight;
                else
                    this.diagonal = Direction.TopLeft;
            if (this.x == clingWhat.x + 20 && this.y == clingWhat.y)
                if (this.diagonal == Direction.TopLeft)
                    this.diagonal = Direction.TopRight;
                else
                    this.diagonal = Direction.BottomRight;
            if (this.x + 20 == clingWhat.x && this.y == clingWhat.y)
                if (this.diagonal == Direction.TopRight)
                    this.diagonal = Direction.TopLeft;
                else
                    this.diagonal = Direction.BottomLeft;
        }

        if (clingTiles.size() == 2)
            this.turnBack();
    }

    /**
     * Turn an enemy back.
     */
    public void turnBack() {
        if (this.diagonal == Direction.TopRight)
            this.diagonal = Direction.BottomLeft;
        else if (this.diagonal == Direction.TopLeft)
            this.diagonal = Direction.BottomRight;
        else if (this.diagonal == Direction.BottomLeft)
            this.diagonal = Direction.TopRight;
        else if (this.diagonal == Direction.BottomRight)
            this.diagonal = Direction.TopLeft;
    }

    /**
     * Get the cling tiles of an enemy and return all tiles it is clinging to.
     * Check paths, grasses, cement.
     *
     * @param app the running app
     * @return all tiles clinging to
     */
    public ArrayList<Tile> getClingTiles(App app) {
        ArrayList<Tile> clingTiles = new ArrayList<Tile>();
        for (Cement cement: app.cementTiles) {
            if (this.checkCling(cement))
                clingTiles.add(cement);
        }

        for (Grass grass: app.grasses) {
            if (this.checkCling(grass))
                clingTiles.add(grass);
        }

        for (Path path: app.paths) {
            if (this.checkCling(path)) {
                clingTiles.add(path);
                path.turnRed(app);
            }
        }
        return clingTiles;
    }

    /**
     * Destroy a grass only once per frame.
     *
     * @param app the running app
     */
    public void destroyGrass(App app) {
        // throw ConcurrentModificationException using for-each
//        app.grasses.removeIf(grass -> this.checkCling(grass) || this.checkCollide(grass));
        for (Grass grass: app.grasses)
            if (this.checkCling(grass) || this.checkCollide(grass)) {
                app.grasses.remove(grass);
                // only remove one
                return;
            }
    }

    /**
     * Update the position per frame.
     */
    public void tick() {
        if (this.diagonal == Direction.TopLeft) {
            this.x -= this.speed;
            this.y -= this.speed;
        }
        if (this.diagonal == Direction.TopRight) {
            this.x += this.speed;
            this.y -= this.speed;
        }
        if (this.diagonal == Direction.BottomLeft) {
            this.x -= this.speed;
            this.y += this.speed;
        }
        if (this.diagonal == Direction.BottomRight) {
            this.x += this.speed;
            this.y += this.speed;
        }
    }

}
