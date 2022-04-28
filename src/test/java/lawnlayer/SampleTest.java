package lawnlayer;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import processing.core.PImage;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SampleTest {

    @Test
    public void testApp() {
        App app = new App();
//        app.noLoop(); //optional
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000); //to give time to initialise stuff before drawing begins
    }

    @Test
    // constructor
    public void testTile() {
        Tile t1 = new Tile(0, 0);
        assertEquals(0, t1.x);

        Tile t2 = new Tile(20, 0, null);
        assertEquals(20, t2.x);
    }

    @Test
    // constructor
    public void testCement() {
        Cement c = new Cement(40, 0, null);
        assertEquals(40, c.x);
    }

    @Test
    // constructor
    public void testGrass() {
        Grass g = new Grass(60, 0, null);
        assertEquals(60, g.x);
    }

    @Test
    // constructor
    public void testBettle() {
        Bettle b = new Bettle(80, 0, null);
        assertEquals(80, b.x);
    }

    @Test
    // constructor, blank grid or not
    public void testCharacter() {
        Character c = new Player(100, 0, null);
        assertEquals(100, c.x);
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        tiles.add(new Tile(120, 0));
        assertTrue(c.existsTile(120, 0, tiles));
        assertFalse(c.existsTile(0, 0, tiles));
    }

    @Test
    // paths can be propagated red and get cling path
    public void testPath() {
        App app = new App();
        Path p = new Path(0, 0, null);
        Path p2 = new Path(20, 0, null);
        Path p3 = new Path(0, 20, null);
        app.paths = new ArrayList<Path>();
        app.paths.add(p);
        app.paths.add(p2);
        app.paths.add(p3);
        p3.turnRed(app);
        assertTrue(p3.isRed);

        ArrayList<Path> willRed = p.getClingPaths(app, p);
        assertEquals(willRed.size(), 1);

        p.propagateRed(app);
        assertTrue(p2.isRed);
    }

    @Test
    // collide
    public void testElement() {
        App app = new App();
        Path p1 = new Path(0, 0, null);
        Path p2 = new Path(10, 0, null);
        Player p = new Player(0, 0, null);
        Enemy e = new Enemy(100, 100, null);
        app.paths = new ArrayList<Path>();
        app.paths.add(p1);
        app.paths.add(p2);
        assertTrue(p1.checkCollide(p2));
        assertTrue(p.checkInRegion(app.paths));
        assertFalse(e.checkInRegion(app.paths));
    }
}
