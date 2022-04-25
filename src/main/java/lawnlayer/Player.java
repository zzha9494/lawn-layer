package lawnlayer;

import processing.core.PImage;

enum Direction {
    Stop,
    Up,
    Down,
    Left,
    Right
}

public class Player extends Character {
    public Direction slideDirection;
    public boolean leftMoving;
    public boolean rightMoving;
    public boolean upMoving;
    public boolean downMoving;

    public int lives;

    public Player(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.slideDirection = Direction.Stop;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void tick() {
        if (this.slideDirection != Direction.Stop)
            this.slideSnapGrid();
        else
            this.keyPressedMoving();
    }

    public void slideSnapGrid() {
        if(this.slideDirection == Direction.Left) {
            if (this.x % 20 ==0) {
                this.slideDirection = Direction.Stop;
                return;
            }
            this.x -= 2;
        }

        if(this.slideDirection == Direction.Right) {
            if (this.x % 20 ==0) {
                this.slideDirection = Direction.Stop;
                return;
            }
            this.x += 2;
        }

        if(this.slideDirection == Direction.Down) {
            if (this.y % 20 ==0) {
                this.slideDirection = Direction.Stop;
                return;
            }
            this.y += 2;
        }

        if(this.slideDirection == Direction.Up) {
            if (this.y % 20 ==0) {
                this.slideDirection = Direction.Stop;
                return;
            }
            this.y -= 2;
        }
    }

    public void keyPressedMoving() {
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


}
