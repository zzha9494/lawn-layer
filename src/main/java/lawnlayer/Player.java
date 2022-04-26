package lawnlayer;

import processing.core.PImage;

public class Player extends Character {
    public Direction leftRightDirection;
    public Direction upDownDirection;
    public Direction soilSlideDirection;
    public boolean turnHead;
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
                this.slideSnapGrid();
//        else {
////            if (this.onCement)
//                this.cementMoving();
////            else
////                this.soilMoving();
//        }
    }

    public void slideSnapGrid() {

        if(this.leftRightDirection == Direction.Left && this.y % 20 ==0) {
            this.x -= 2;
            if (this.x % 20 ==0 && !this.leftMoving)
                this.leftRightDirection = Direction.Stop;
        }

        if(this.leftRightDirection == Direction.Right  && this.y % 20 ==0) {
            this.x += 2;
            if (this.x % 20 ==0 && !this.rightMoving)
                this.leftRightDirection = Direction.Stop;
        }

        if(this.upDownDirection == Direction.Up && this.x % 20 ==0) {
            this.y -= 2;
            if (this.y % 20 ==0 && !this.upMoving)
                this.upDownDirection = Direction.Stop;
        }

        if(this.upDownDirection == Direction.Down && this.x % 20 ==0) {
            this.y += 2;
            if (this.y % 20 ==0 && !this.downMoving) {
                this.upDownDirection = Direction.Stop;
            }
        }

    }

    public void soilMoving() {
        if (this.soilSlideDirection == Direction.Left)
            this.x -= 2;
        if (this.soilSlideDirection == Direction.Right)
            this.x += 2;
        if (this.soilSlideDirection == Direction.Up)
            this.y -= 2;
        if (this.soilSlideDirection == Direction.Down)
            this.y += 2;
    }

    public void cementMoving() {
        if (this.leftMoving || this.rightMoving) {
            if (this.leftMoving  && this.x > 0)
                this.x -= 2;
            if (this.rightMoving && this.x < 1260)
                this.x += 2;
        } else if (this.upMoving || this.downMoving) {
            if (this.upMoving  && this.y > 80)
                this.y -= 2;
            if (this.downMoving  && this.y < 700)
                this.y += 2;
        }
    }

    public void checkOnCement(App app) {
        if (this.collideCement(app) != null) {
            this.onCement = true;
            this.soilSlideDirection = Direction.Stop;
        }
        else
            this.onCement = false;
    }

}
