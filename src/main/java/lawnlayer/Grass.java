package lawnlayer;

import processing.core.PImage;

/**
 * This class is inherited from {@code Tile} class.
 * A grass tile can be created by a player on condition
 * and be destroyed by a bettle.
 *
 * @author Zijie Zhao
 * @version 1.0
 * @since 22/04/2022
 */
public class Grass extends Tile{

    /**
     * Class constructor.
     *
     * @param x the x coordinate of this grass
     * @param y the y coordinate of this grass
     * @param sprite the image of this grass
     */
    public Grass(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

}
