package lawnlayer;

import processing.core.PImage;

import java.util.ArrayList;

/**
 * This class is inherited from {@code Tile} class.
 * A path tile can be created by a player during it is moving on empty tile or on grass.
 * Path tile is either green or red.
 *
 * @author Zijie Zhao
 * @version 1.0
 * @since 22/04/2022
 */
public class Path extends Tile{
    public Boolean isRed;

    /**
     * Class constructor.
     *
     * @param x the x coordinate of this path
     * @param y the x coordinate of this path
     * @param sprite the image of this path
     */
    public Path(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.isRed = false;
    }

    /**
     * Turn a path tile to red and change its image.
     *
     * @param app the running app
     */
    public void turnRed(App app) {
        this.isRed = true;
        this.sprite = app.red;
    }

    /**
     * Starts propagation from all current red paths.
     *
     * @param app the running app
     */
    public void propagateRed(App app) {
        ArrayList<Path> willRed = getClingPaths(app, this);
        for (Path p: willRed)
            p.turnRed(app);
    }

    /**
     * Get the cling green paths of a red path and return all green paths clinging to.
     * The path nearby can be one of four direction(Left, Up, Right, Down).
     *
     * @param app the running app
     * @param redPath a red path that is the origin
     * @return all the green paths clinging to
     */
    public ArrayList<Path> getClingPaths(App app, Path redPath) {
        ArrayList<Path> clingPaths = new ArrayList<Path>();
        for (Path p: app.paths)
            if (redPath.checkCling(p) && !p.isRed)
                clingPaths.add(p);
        return clingPaths;
    }

}
