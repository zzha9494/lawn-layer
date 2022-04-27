package lawnlayer;

import processing.core.PImage;

import java.util.ArrayList;

public class Player extends Character {
    public int lives;
    public boolean alive;

    public boolean centerCement;
    public boolean centerGrass;
    public boolean hitCement;
    public boolean duringPowerup;

    public boolean leftMoving;
    public boolean rightMoving;
    public boolean upMoving;
    public boolean downMoving;
    public Direction leftRightDirection;
    public Direction upDownDirection;
    public Direction slideDirection;
    public Direction turnDirection;

    public ArrayList<Tile> floodArea;

    public Player(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.alive = true;
        this.leftRightDirection = Direction.Stop;
        this.upDownDirection = Direction.Stop;
        this.floodArea = new ArrayList<Tile>();
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void updatePositionFlag(App app) {
        this.hitCement = this.checkInRegion(app.cementTiles);
        this.centerCement = this.existsTile(this.x, this.y, app.cementTiles);
        this.centerGrass = this.existsTile(this.x, this.y, app.grasses);
    }

    public void normalMoving() {
        if(this.centerCement) {
            this.slideDirection = Direction.Stop;
            this.turnDirection = Direction.Stop;
        }

        if(this.leftRightDirection == Direction.Left && this.y % 20 ==0 && this.x > 0) {
            if (this.centerCement)
                this.slideDirection = Direction.Left;
            this.x -= 2;
            if (this.x % 20 ==0 && !this.leftMoving)
                this.leftRightDirection = Direction.Stop;
        }

        else if(this.leftRightDirection == Direction.Right  && this.y % 20 ==0 && this.x < 1260) {
            if (this.centerCement)
                this.slideDirection = Direction.Right;
            this.x += 2;
            if (this.x % 20 ==0 && !this.rightMoving)
                this.leftRightDirection = Direction.Stop;
        }

        else if(this.upDownDirection == Direction.Up && this.x % 20 ==0 && this.y > 80) {
            if (this.centerCement)
                this.slideDirection = Direction.Up;
            this.y -= 2;
            if (this.y % 20 ==0 && !this.upMoving)
                this.upDownDirection = Direction.Stop;
        }

        else if(this.upDownDirection == Direction.Down && this.x % 20 ==0 && this.y < 700) {
            if (this.centerCement)
               this.slideDirection = Direction.Down;
            this.y += 2;
            if (this.y % 20 ==0 && !this.downMoving) {
                this.upDownDirection = Direction.Stop;
            }
        }
    }

    public void soilMoving() {
        if(!this.hitCement) {
            this.leftRightDirection = Direction.Stop;
            this.upDownDirection = Direction.Stop;
        }

        if (this.x % 20 == 0 && this.y % 20 == 0)
            this.slideTurnDirection();

        if(this.slideDirection == Direction.Left) {
            this.x -= 2;
            this.leftRightDirection = Direction.Left;
        }

        if(this.slideDirection == Direction.Right) {
            this.x += 2;
            this.leftRightDirection = Direction.Right;
        }

        if(this.slideDirection == Direction.Up) {
            this.y -= 2;
            this.upDownDirection = Direction.Up;
        }

        if(this.slideDirection == Direction.Down) {
            this.y += 2;
            this.upDownDirection = Direction.Down;
        }
    }

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

    public void createGrass(App app) {
        for (Path path: app.paths) {
            Grass grass = new Grass(path.x, path.y, app.grass);
            app.grasses.add(grass);
        }

        for (Path path: app.paths)
            this.captureTerritory(app, path);
    }


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

    public boolean isPlainSoil(App app, int x, int y) {
        if(this.existsTile(x, y, app.cementTiles))
            return false;
        if(this.existsTile(x, y, app.grasses))
            return false;
        if(this.existsTile(x, y, this.floodArea))
            return false;
        return true;
    }

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


    public void checkLoseOneLife(App app) {
        if(this.checkInRegion(app.enemies))
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

    public void playerRespawn(App app) {
        this.lives --;
        if (this.lives == 0) {
            app.gameOver = true;
            return;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.moveOrigin(app);
    }

    public void moveOrigin(App app) {
        this.x = 0;
        this.y = 80;
        this.leftRightDirection = Direction.Stop;
        this.upDownDirection = Direction.Stop;
        this.slideDirection = Direction.Stop;
        this.turnDirection = Direction.Stop;
        this.alive = true;
        this.duringPowerup = false;
        app.paths.clear();
        app.powerup = null;
        app.powerSpawnTimer = 0;
        app.randomInterval = 0;
    }


    public void tick() {
        if(this.hitCement)
            this.normalMoving();
        else
            this.soilMoving();
    }
}
