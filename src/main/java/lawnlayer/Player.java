package lawnlayer;

import processing.core.PImage;

public class Player extends Character {
    public Direction leftRightDirection;
    public Direction upDownDirection;
    public Direction soilSlideDirection;
    public Direction turn;
    public boolean centerCement;
    public boolean onCement;
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
//        if (this.slideDirection != Direction.Stop)
//        if (this.centerCement)
//            this.soilSlideDirection = Direction.Stop;

        if(this.onCement)
            this.cementMoving();
        else
            this.soilMoving();
    }

    public void cementMoving() {
        if(this.centerCement) {
            this.soilSlideDirection = Direction.Stop;
            this.turn = Direction.Stop;
        }


        if(this.leftRightDirection == Direction.Left && this.y % 20 ==0) {
            if (this.centerCement)
                this.soilSlideDirection = Direction.Left;
            this.x -= 2;
            if (this.x % 20 ==0 && !this.leftMoving)
                this.leftRightDirection = Direction.Stop;
        }

        if(this.leftRightDirection == Direction.Right  && this.y % 20 ==0) {
            if (this.centerCement)
                this.soilSlideDirection = Direction.Right;
            this.x += 2;
            if (this.x % 20 ==0 && !this.rightMoving)
                this.leftRightDirection = Direction.Stop;
        }

        if(this.upDownDirection == Direction.Up && this.x % 20 ==0) {
            if (this.centerCement)
                this.soilSlideDirection = Direction.Up;
            this.y -= 2;
            if (this.y % 20 ==0 && !this.upMoving)
                this.upDownDirection = Direction.Stop;
        }

        if(this.upDownDirection == Direction.Down && this.x % 20 ==0) {
            if (this.centerCement)
               this.soilSlideDirection = Direction.Down;
            this.y += 2;
            if (this.y % 20 ==0 && !this.downMoving) {
                this.upDownDirection = Direction.Stop;
            }
        }

    }

    public void soilMoving() {
        if(!this.onCement) {
            this.leftRightDirection = Direction.Stop;
            this.upDownDirection = Direction.Stop;
        }

        if (this.x % 20 == 0 && this.y % 20 == 0)
            this.slideTurnDirection();

        if(this.soilSlideDirection == Direction.Left) {
            this.x -= 2;
            this.leftRightDirection = Direction.Left;
        }

        if(this.soilSlideDirection == Direction.Right) {
            this.x += 2;
            this.leftRightDirection = Direction.Right;
        }

        if(this.soilSlideDirection == Direction.Up) {
            this.y -= 2;
            this.upDownDirection = Direction.Up;
        }

        if(this.soilSlideDirection == Direction.Down) {
            this.y += 2;
            this.upDownDirection = Direction.Down;
        }
    }

    public void slideTurnDirection() {
        if (this.turn == Direction.Left) {
            this.soilSlideDirection = Direction.Left;
            this.turn = Direction.Stop;
        }
        if (this.turn == Direction.Right) {
            this.soilSlideDirection = Direction.Right;
            this.turn = Direction.Stop;
        }
        if (this.turn == Direction.Up) {
            this.soilSlideDirection = Direction.Up;
            this.turn = Direction.Stop;
        }
        if (this.turn == Direction.Down) {
            this.soilSlideDirection = Direction.Down;
            this.turn = Direction.Stop;
        }
    }

    public void checkOnCement(App app) {

        if (this.collideCement(app) != null) {
            this.onCement = true;
        }
        else
            this.onCement = false;

        for(Cement c: app.cementTiles){
            if(c.x == this.x && c.y == this.y) {
                this.centerCement = true;
                return;
            }
        }
        this.centerCement = false;
    }

}
