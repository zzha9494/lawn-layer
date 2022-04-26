package lawnlayer;

import processing.core.PImage;

import java.util.ArrayList;

public class Player extends Character {
    public Direction leftRightDirection;
    public Direction upDownDirection;
    public Direction slideDirection;
    public Direction turnDirection;

    public boolean leftMoving;
    public boolean rightMoving;
    public boolean upMoving;
    public boolean downMoving;

    public boolean centerCement;
    public boolean centerGrass;
    public boolean hitCement;

    public ArrayList<Tile> tempGrasses;

    public int lives;

    public Player(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.leftRightDirection = Direction.Stop;
        this.upDownDirection = Direction.Stop;
        this.tempGrasses = new ArrayList<Tile>();
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void tick() {
        if(this.hitCement)
            this.cementMoving();
        else
            this.soilMoving();
    }

    public void cementMoving() {
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

    public void checkOnTile(App app) {
        if (this.collideCement(app) != null)
            this.hitCement = true;
        else
            this.hitCement = false;

        for(Cement c: app.cementTiles){
            if(c.x == this.x && c.y == this.y) {
                this.centerCement = true;
                break;
            }
            else
                this.centerCement = false;
        }

        for (Grass g: app.grasses) {
            if(g.x == this.x && g.y == this.y) {
                this.centerGrass = true;
                break;
            }
            else
                this.centerGrass = false;
        }

    }

    public void createPath(App app) {
        if ((this.centerCement || this.centerGrass) && app.paths.size() != 0) {
            this.createGrass(app);
            app.paths.clear();
            return;
        }

        if (this.x % 20 == 0 && this.y % 20 == 0) {
            boolean createOne = true;

            for (Cement cement: app.cementTiles) {
                if (this.x == cement.x && this.y == cement.y) {
                    createOne = false;
                    break;
                }
            }
            if (createOne) {
                for (Grass grass: app.grasses) {
                    if (this.x == grass.x && this.y == grass.y) {
                        createOne = false;
                        break;
                    }
                }
            }

            if (createOne) {
                Path path = new Path(this.x, this.y, app.green);
                app.paths.add(path);
            }
        }
    }

    public void createGrass(App app) {
        for (Path path: app.paths) {
            Grass grass = new Grass(path.x, path.y, app.grass);
            app.grasses.add(grass);
        }

        for (Path path: app.paths)
            this.floodFill(app, path);
    }

    public void floodFill(App app, Path p) {
        boolean encloseSuccess = false;
        if(!existsTile(app, p.x-20, p.y)) {
            this.tempGrasses.add(new Tile(p.x-20, p.y));
            this.contagious(app, this.tempGrasses.get(0));
            encloseSuccess = this.encloseRegion(app);
        }
        if(!existsTile(app, p.x+20, p.y) && !encloseSuccess) {
            this.tempGrasses.add(new Tile(p.x+20, p.y));
            this.contagious(app, this.tempGrasses.get(0));
            encloseSuccess = this.encloseRegion(app);
        }
        if(!existsTile(app, p.x, p.y+20) && !encloseSuccess) {
            this.tempGrasses.add(new Tile(p.x, p.y+20));
            this.contagious(app, this.tempGrasses.get(0));
            encloseSuccess = this.encloseRegion(app);
        }
        if(!existsTile(app, p.x, p.y-20) && !encloseSuccess) {
            this.tempGrasses.add(new Tile(p.x, p.y-20));
            this.contagious(app, this.tempGrasses.get(0));
            this.encloseRegion(app);
        }
    }

    public void contagious(App app, Tile t) {
        if(!existsTile(app, t.x-20, t.y)) {
            Tile temp = new Tile(t.x-20, t.y);
            this.tempGrasses.add(temp);
            contagious(app, temp);
        }
        if(!existsTile(app, t.x+20, t.y)) {
            Tile temp = new Tile(t.x+20, t.y);
            this.tempGrasses.add(temp);
            contagious(app, temp);
        }
        if(!existsTile(app, t.x, t.y+20)) {
            Tile temp = new Tile(t.x, t.y+20);
            this.tempGrasses.add(temp);
            contagious(app, temp);
        }
        if(!existsTile(app, t.x, t.y-20)) {
            Tile temp = new Tile(t.x, t.y-20);
            this.tempGrasses.add(temp);
            contagious(app, temp);
        }
    }

    public boolean existsTile(App app, int x, int y) {
        for (Cement cement: app.cementTiles) {
            if (cement.x == x && cement.y == y)
                return true;
        }
        for (Grass grass: app.grasses) {
            if (grass.x == x && grass.y == y)
                return true;
        }

        for (Tile t: this.tempGrasses) {
            if (t.x == x && t.y == y)
                return true;
        }
        return false;
    }

    public boolean encloseRegion(App app) {
        boolean allClear = true;
        for (Enemy enemy: app.enemies) {
            if (enemy.checkInRegion(this.tempGrasses)) {
                tempGrasses.clear();
                allClear = false;
                break;
            }
        }

        if (allClear) {
            for (Tile tile: tempGrasses)
                app.grasses.add(new Grass(tile.x, tile.y, app.grass));
            tempGrasses.clear();
            return true;
        }
        return false;
    }

}
