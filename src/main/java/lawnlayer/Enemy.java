package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

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

    public ArrayList<Tile> clingTiles(App app) {
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

    public void changeDiagonal(App app) {
        ArrayList<Tile> clingTiles = this.clingTiles(app);
        if (clingTiles.size() == 0)
            return;

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

        if (clingTiles.size() == 2) {
            if (this.diagonal == Direction.TopRight)
                this.diagonal = Direction.BottomLeft;
            else if (this.diagonal == Direction.TopLeft)
                this.diagonal = Direction.BottomRight;
            else if (this.diagonal == Direction.BottomLeft)
                this.diagonal = Direction.TopRight;
            else if (this.diagonal == Direction.BottomRight)
                this.diagonal = Direction.TopLeft;
        }
    }

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

    public boolean checkInRegion (ArrayList<Tile> region) {
        for (Tile t: region) {
            if (this.checkCollide(t))
                return true;
        }
        return false;
    }

}
