package lawnlayer;

import processing.core.PImage;

public class Player extends Character {
    public Direction leftRightDirection;
    public Direction upDownDirection;
    public Direction slideDirection;
    public Direction turnDirection;
    public boolean centerCement;
    public boolean hitCement;
    public boolean leftMoving;
    public boolean rightMoving;
    public boolean upMoving;
    public boolean downMoving;

    public int lives;

    public Player(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.leftRightDirection = Direction.Stop;
        this.upDownDirection = Direction.Stop;
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

        if(this.leftRightDirection == Direction.Left && this.y % 20 ==0) {
            if (this.centerCement)
                this.slideDirection = Direction.Left;
            this.x -= 2;
            if (this.x % 20 ==0 && !this.leftMoving)
                this.leftRightDirection = Direction.Stop;
        }

        else if(this.leftRightDirection == Direction.Right  && this.y % 20 ==0) {
            if (this.centerCement)
                this.slideDirection = Direction.Right;
            this.x += 2;
            if (this.x % 20 ==0 && !this.rightMoving)
                this.leftRightDirection = Direction.Stop;
        }

        else if(this.upDownDirection == Direction.Up && this.x % 20 ==0) {
            if (this.centerCement)
                this.slideDirection = Direction.Up;
            this.y -= 2;
            if (this.y % 20 ==0 && !this.upMoving)
                this.upDownDirection = Direction.Stop;
        }

        else if(this.upDownDirection == Direction.Down && this.x % 20 ==0) {
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

    public void checkOnCement(App app) {
        if (this.collideCement(app) != null)
            this.hitCement = true;
        else
            this.hitCement = false;

        for(Cement c: app.cementTiles){
            if(c.x == this.x && c.y == this.y) {
                this.centerCement = true;
                return;
            }
        }
        this.centerCement = false;
    }

    public void createPath(App app) {
        if (this.centerCement && app.paths.size() != 0) {
            this.createGrass(app);
            app.paths.clear();
        }

        if (this.x % 20 == 0 && this.y % 20 == 0) {
            for (Cement cement: app.cementTiles) {
                if (this.x == cement.x && this.y == cement.y)
                    return;
            }
            Path path = new Path(this.x, this.y, app.green);
            app.paths.add(path);
        }
    }

    public void createGrass(App app) {
        for (Path path: app.paths) {
            Grass grass = new Grass(path.x, path.y, app.grass);
            app.grasses.add(grass);
        }
    }

}
