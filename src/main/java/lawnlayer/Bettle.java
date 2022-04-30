package lawnlayer;

import processing.core.PImage;

/**
 * This class is inherited from {@code enemy} class.
 * Any bettle whose type is set to 1.
 *
 * @author Zijie Zhao
 * @version 1.0
 * @since 22/04/2022
 */
public class Bettle extends Enemy{

    /**
     * Class constructor.
     *
     * @param x the x coordinate of this bettle
     * @param y the y coordinate of this bettle
     * @param sprite the image coordinate of this bettle
     */
    public Bettle(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.type = 1;
    }

}
