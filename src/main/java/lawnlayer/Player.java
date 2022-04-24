package lawnlayer;

import processing.core.PImage;

public class Player extends Character {
    public boolean moveLeft;
    public boolean moveUp;
    public boolean moveRight;
    public boolean moveDown;

    public Player(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

    public void tick() {
        if (moveLeft || moveRight) {
            if (moveLeft)
                this.x -= 2;
            if (moveRight)
                this.x += 2;
        } else if (moveUp || moveDown) {
            if (moveUp)
                this.y -= 2;
            if (moveDown)
                this.y += 2;
        }
    }


}
