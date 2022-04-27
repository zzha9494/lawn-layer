package lawnlayer;

import processing.core.PImage;

import java.util.ArrayList;

public class Path extends Tile{
    public Boolean isRed;

    public Path(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.isRed = false;
    }

    public void turnRed(App app) {
        this.isRed = true;
        this.sprite = app.red;
    }

    public void propagateRed(App app) {
        ArrayList<Path> willRed = getClingPaths(app, this);
        for (Path p: willRed)
            p.turnRed(app);
    }

    public ArrayList<Path> getClingPaths(App app, Path redPath) {
        ArrayList<Path> clingPaths = new ArrayList<Path>();
        for (Path p: app.paths)
            if (redPath.checkCling(p) && !p.isRed)
                clingPaths.add(p);
        return clingPaths;
    }

}
