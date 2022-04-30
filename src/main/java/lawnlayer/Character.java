package lawnlayer;

import processing.core.PImage;

import java.util.ArrayList;

/**
 * This is an abstract class that express any dynamic elements.
 *
 * @author Zijie Zhao
 * @version 1.0
 * @since 22/04/2022
 */
public abstract class Character extends Element{

    /**
     * Class constructor.
     *
     * @param x the x coordinate of the character
     * @param y the y coordinate of the character
     * @param sprite the image of the character
     */
    public Character(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

    /**
     * This method is to check whether the specific position exists an element
     * that also in a region.
     *
     * @param x the x coordinate of the position
     * @param y the x coordinate of the position
     * @param region a set of elements you want to check
     * @param <T> any element
     * @return {@code true} if an element in the region has the specific position
     */
    public <T extends Element> boolean existsTile(int x, int y, ArrayList<T> region) {
        for (T t: region)
            if (t.x == x && t.y == y)
                return true;
        return false;
    }

    /**
     * This method is to update the moving of the dynamic element per frame.
     */
    public abstract void tick();

}
