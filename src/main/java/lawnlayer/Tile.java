package lawnlayer;

import processing.core.PImage;

/**
 * This is a class that express any static elements.
 *
 * @author Zijie Zhao
 * @version 1.0
 * @since 22/04/2022
 */
public class Tile extends Element{

    /**
     * Class constructor.
     *
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     */
    public Tile(int x, int y) {
        super(x, y);
    }

    /**
     * Class constructor.
     *
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @param sprite the image of the tile
     */
    public Tile(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

}
