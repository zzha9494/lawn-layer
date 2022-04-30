package lawnlayer;

import processing.core.PImage;

/**
 * This class is inherited from {@code Tile} class.
 * A cement tile is a concrete that consists of the map
 * and cannot be destroyed.
 *
 * @author Zijie Zhao
 * @version 1.0
 * @since 22/04/2022
 */
public class Cement extends Tile {

    /**
     * Class constructor.
     *
     * @param x the x coordinate of this cement
     * @param y the y coordinate of this cement
     * @param sprite the image of this cement
     */
    public Cement(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

}
