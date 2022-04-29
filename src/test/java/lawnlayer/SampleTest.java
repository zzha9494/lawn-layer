package lawnlayer;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import processing.event.KeyEvent;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SampleTest {

    @Test
    // win condition check
    public void testApp_2() {
        App app = new App();
        app.noLoop(); //optional
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

//        KeyEvent k = new KeyEvent(null, 1000, KeyEvent.PRESS, 0, '\uffff', 37);
        app.player.slideDirection = Direction.Down;
        app.keyCode = 37;
        assertFalse(app.player.leftMoving);
        app.keyPressed();
        assertTrue(app.player.leftMoving);
        assertEquals(Direction.Left, app.player.turnDirection);
        app.player.slideTurnDirection();
        app.keyReleased();
        assertFalse(app.player.leftMoving);

        app.keyCode = 38;
        assertFalse(app.player.upMoving);
        app.keyPressed();
        assertTrue(app.player.upMoving);
        assertEquals(Direction.Up, app.player.turnDirection);
        app.player.slideTurnDirection();
        app.keyReleased();
        assertFalse(app.player.upMoving);

        app.keyCode = 39;
        assertFalse(app.player.rightMoving);
        app.keyPressed();
        assertTrue(app.player.rightMoving);
        assertEquals(Direction.Right, app.player.turnDirection);
        app.player.slideTurnDirection();
        app.keyReleased();
        assertFalse(app.player.rightMoving);

        app.keyCode = 40;
        assertFalse(app.player.downMoving);
        app.keyPressed();
        assertTrue(app.player.downMoving);
        assertEquals(Direction.Down, app.player.turnDirection);
        app.player.slideTurnDirection();
        app.keyReleased();
        assertFalse(app.player.downMoving);

        // win, over check
        app.currentLevel = 1;
        assertFalse(app.gameWin);

        assertFalse(app.gameOver);
        app.player.lives = 0;
        app.winConditionCheck();
        assertTrue(app.gameOver);
        app.showText();

        app.keyCode = 80;
        app.keyPressed();
        assertFalse(app.gameOver);

        int a = (int)Math.round((32*64 - app.cementTiles.size()) * app.goal/100);
        for (int i = 0; i < a; i++)
            app.grasses.add(new Grass(0, 0, app.grass));
        app.winConditionCheck();
        assertEquals(2, app.currentLevel);
        int b = (int)Math.round((32*64 - app.cementTiles.size()) * app.goal/100);
        for (int i = 0; i < a; i++)
            app.grasses.add(new Grass(0, 0, app.grass));
        assertFalse(app.gameWin);
        app.winConditionCheck();
        assertTrue(app.gameWin);
        app.showText();
    }

    @Test
    // propagate event, power event
    public void testApp_1() {
        App app = new App();
        app.noLoop(); //optional
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

        // get red path
        app.enemies.clear();
        assertEquals(0, app.currentRed.size());
        app.paths.add(new Path(0, 0, null));
        app.paths.get(0).turnRed(app);
        app.currentRed = app.getCurrentRed();
        assertEquals(1, app.currentRed.size());

        // propagate
        assertEquals(3, app.propagationSpeed);
        app.currentRed.clear();
        assertEquals(1, app.paths.size());
        assertEquals(0, app.propagateTimer);
        app.propagateTimerIncrease();
        //start propagate 3 frames
        assertEquals(1, app.propagateTimer);
        assertEquals(0, app.currentRed.size());
        app.propagateTimerIncrease();
        app.propagateTimerIncrease();
        app.propagateTimerIncrease();
        assertEquals(1, app.propagateTimer);
        assertEquals(1, app.currentRed.size());
        app.paths.get(0).isRed = false;
        app.currentRed.clear();
        app.propagateTimerIncrease();
        app.propagateTimerIncrease();
        app.propagateTimerIncrease();
        assertEquals(1, app.propagateTimer);
        assertEquals(0, app.currentRed.size());
        app.paths.clear();
        app.currentRed.clear();

        // power event
        app.powerupSpawnTimer = 0;
        app.randomInterval = 0;
        assertNull(app.unCollectedPowerup);
        app.powerupEvent();
        assertNotEquals(0, app.randomInterval);
        assertEquals(1, app.powerupSpawnTimer);

        app.powerupSpawnTimer = app.randomInterval - 1;
        app.powerupEvent();
        assertNotNull(app.unCollectedPowerup);
        assertEquals(0, app.randomInterval);

        app.powerupEvent();
        assertNotEquals(0, app.randomInterval);

        app.collectedPowerup = app.unCollectedPowerup;
        app.draw();
        app.unCollectedPowerup = null;
        app.powerupDurationTimer = 0;
        app.powerupEvent();
        assertEquals(1, app.powerupDurationTimer);

        assertNotNull(app.collectedPowerup);
        app.powerupDurationTimer = 599;
        app.powerupEvent();
        assertNull(app.collectedPowerup);
    }

    @Test
    // player moving
    public void testPlayer_3() {
        App app = new App();
        app.noLoop(); //optional
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

        // 3x3 cement grid with empty space
        app.enemies.clear();
        app.cementTiles.clear();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                app.cementTiles.add(new Cement(20*i, 80 + 20*j, null));
        assertEquals(9, app.cementTiles.size());

        // normal moving
        app.player.leftRightDirection = Direction.Right;
        app.player.tick();
        assertEquals(Direction.Right, app.player.slideDirection);
        for(int i = 0; i < 9; i++)
            app.player.tick();
        assertEquals(20, app.player.x);

        app.player.leftRightDirection = Direction.Left;
        app.player.tick();
        assertEquals(Direction.Left, app.player.slideDirection);
        for(int i = 0; i < 9; i++)
            app.player.tick();
        assertEquals(0, app.player.x);

        app.player.upDownDirection = Direction.Down;
        app.player.tick();
        assertEquals(Direction.Down, app.player.slideDirection);
        for(int i = 0; i < 9; i++)
            app.player.tick();
        assertEquals(100, app.player.y);

        app.player.upDownDirection = Direction.Up;
        app.player.tick();
        assertEquals(Direction.Up, app.player.slideDirection);
        for(int i = 0; i < 9; i++)
            app.player.tick();
        assertEquals(80, app.player.y);

        // soil moving, turn back direction is constrained by key pressed see App.java
        app.cementTiles.clear();
        app.player.moveOrigin(app);
        for (int i = 0; i < 5; i++) {
            app.cementTiles.add(new Cement(20*i, 80, null));
            app.cementTiles.add(new Cement(20*i, 160, null));
        }
        for (int i = 0; i < 3; i++) {
            app.cementTiles.add(new Cement(0, 100+ 20*i, null));
            app.cementTiles.add(new Cement(80, 100+ 20*i, null));
        }
        assertEquals(16, app.cementTiles.size());

        app.player.x = 40;
        app.player.upDownDirection = Direction.Down;
        for(int i = 0; i < 20; i++) {
            app.player.tick();
            app.player.updatePositionFlag(app);
        }
        assertEquals(120, app.player.y);
        assertEquals(Direction.Down, app.player.slideDirection);
        assertFalse(app.player.hitCement);

        app.player.turnDirection = Direction.Up;
        app.player.slideTurnDirection();
        assertEquals(Direction.Up, app.player.slideDirection);
        app.player.turnDirection = Direction.Down;
        app.player.slideTurnDirection();
        assertEquals(Direction.Down, app.player.slideDirection);
        app.player.turnDirection = Direction.Right;
        app.player.slideTurnDirection();
        assertEquals(Direction.Right, app.player.slideDirection);
        for(int i = 0; i < 20; i++) {
            app.player.tick();
            app.player.updatePositionFlag(app);
        }
        assertEquals(80, app.player.x);

        app.player.moveOrigin(app);
        app.player.x = 40;
        app.player.y = 160;
        app.player.upDownDirection = Direction.Up;
        for(int i = 0; i < 20; i++) {
            app.player.tick();
            app.player.updatePositionFlag(app);
        }
        assertEquals(120, app.player.y);
        assertEquals(Direction.Up, app.player.slideDirection);
        app.player.turnDirection = Direction.Left;
        app.player.slideTurnDirection();
        assertEquals(Direction.Left, app.player.slideDirection);
        for(int i = 0; i < 20; i++) {
            app.player.tick();
            app.player.updatePositionFlag(app);
        }
        assertEquals(0, app.player.x);
    }

    @Test
    // create paths, create grasses with and without enemy.
    public void testPlayer_2() {
        App app = new App();
        app.noLoop(); //optional
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

        // 5x5 cement grid with empty space
        app.enemies.clear();
        app.cementTiles.clear();
        for (int i = 0; i < 5; i++) {
            app.cementTiles.add(new Cement(20*i, 0, null));
            app.cementTiles.add(new Cement(20*i, 80, null));
        }
        for (int i = 0; i < 3; i++) {
            app.cementTiles.add(new Cement(0, 20+ 20*i, null));
            app.cementTiles.add(new Cement(80, 20+ 20*i, null));
        }
        for(Cement cement: app.cementTiles)
            cement.sprite = app.concrete;

        assertEquals(16, app.cementTiles.size());
        app.enemies.add(new Bettle(60, 60, app.beetle));
        assertEquals(1, app.enemies.size());
        assertEquals(0, app.grasses.size());

        app.player.x = 40;
        app.player.y = 10;
        app.player.updatePositionFlag(app);
        app.player.createPath(app);
        assertEquals(0, app.paths.size());
        app.player.y = 20;
        app.player.updatePositionFlag(app);
        app.player.createPath(app);
        assertEquals(1, app.paths.size());
        app.player.y = 40;
        app.player.updatePositionFlag(app);
        app.player.createPath(app);
        assertEquals(2, app.paths.size());
        app.player.x = 20;
        app.player.updatePositionFlag(app);
        app.player.createPath(app);
        assertEquals(3, app.paths.size());
        app.player.x = 0;
        app.player.updatePositionFlag(app);
        app.player.createPath(app);
        assertEquals(0, app.paths.size());
        assertEquals(4, app.grasses.size());
        app.draw();

        app.player.x = 40;
        app.player.y = 60;
        app.player.updatePositionFlag(app);
        app.player.createPath(app);
        app.player.y = 50;
        app.player.updatePositionFlag(app);
        app.player.createPath(app);
        assertEquals(4, app.grasses.size());

        app.player.y = 40;
        app.player.updatePositionFlag(app);
        app.player.createPath(app);
        assertEquals(6, app.grasses.size());

        app.grasses.clear();
        app.enemies.get(0).x = 20;
        app.enemies.get(0).y = 20;
        app.paths.add(new Path(40, 40, app.green));
        app.paths.add(new Path(60, 40, null));
        app.paths.add(new Path(40, 60, null));
        assertEquals(3, app.paths.size());
        app.player.createPath(app);

        assertEquals(0, app.paths.size());
        app.player.x = 40;
        app.player.y = 40;
        assertEquals(4, app.grasses.size());
        app.player.createPath(app);
        assertEquals(4, app.grasses.size());

        app.paths.clear();
        app.paths.add(new Path(0, 0, app.red));
        app.paths.get(0).isRed = true;
        app.currentRed.add(app.paths.get(0));
        app.player.centerCement = false;
        app.player.centerGrass = false;
        app.draw();
    }

    @Test
    //move oririn, lose lives, respawn
    public void testPlayer_1() {
        App app = new App();
        app.noLoop(); //optional
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

        // check move orgin
        app.cementTiles.clear();
        app.enemies.clear();
        app.collectedPowerup = new Powerup(app);
        app.player.x = 0;
        app.player.y = 0;
        app.player.updatePositionFlag(app);
        assertFalse(app.player.centerCement);
        app.player.moveOrigin(app);
        assertNull(app.collectedPowerup);
        assertEquals(80, app.player.y);

        // check respawn
        assertEquals(3, app.player.lives);
        app.player.y = 0;
        app.player.playerRespawn(app);
        assertEquals(2, app.player.lives);
        app.player.lives = 1;
        app.player.y = 0;
        app.player.playerRespawn(app);
        assertEquals(0, app.player.y);

        // check lose life
        app.player.x = 0;
        app.player.y = 0;
        app.player.lives = 3;
        app.player.alive = true;
        Bettle bettle = new Bettle(0, 0, null);
        app.enemies.add(bettle);
        assertEquals(1, app.enemies.size());
        assertFalse(app.player.centerCement);
        app.player.checkLoseOneLife(app);
        assertEquals(2, app.player.lives);
        app.currentRed.add(new Path(0, 80, null));
        app.player.checkLoseOneLife(app);
        assertEquals(1, app.player.lives);
        assertEquals(0, app.paths.size());
        app.paths.add(new Path(0, 20, null));
        app.paths.add(new Path(0, 40, null));
        app.player.y = 40;
        assertEquals(1, app.player.lives);
        app.player.checkLoseOneLife(app);
        assertEquals(1, app.player.lives);
        app.player.y = 20;
        app.player.checkLoseOneLife(app);
        assertEquals(0, app.player.lives);
    }

    @Test
    // reflection, destroy grass, turn back
    public void testEnemy() {
        App app = new App();
        app.noLoop(); //optional
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

        // destroy grasses
        assertEquals(0, app.grasses.size());
        for (int i = 0; i < 3; i++)
            for (int j = 0; j< 3; j++)
                app.grasses.add(new Grass(20*i, 20*j, null));
        assertEquals(9, app.grasses.size());
        Bettle bettle = new Bettle(20, 20, null);
        for (int i = 0; i < 6; i++)
            bettle.destroyGrass(app);
        assertEquals(4, app.grasses.size());

        // reflect convex, turn back
        bettle.diagonal = Direction.TopLeft;
        bettle.tick();
        assertEquals(18, bettle.x);
        assertEquals(Direction.TopLeft, bettle.diagonal);
        bettle.reflectDirection(app);
        assertEquals(Direction.BottomRight, bettle.diagonal);
        bettle.tick();
        bettle.tick();
        bettle.reflectDirection(app);
        assertEquals(Direction.TopLeft, bettle.diagonal);
        bettle.tick();
        assertEquals(20, bettle.y);

        bettle.diagonal = Direction.TopRight;
        bettle.tick();
        bettle.reflectDirection(app);
        assertEquals(Direction.BottomLeft, bettle.diagonal);
        bettle.tick();
        bettle.tick();
        bettle.reflectDirection(app);
        assertEquals(Direction.TopRight, bettle.diagonal);
        bettle.tick();
        assertEquals(20, bettle.y);

        // get cling tiles, reflect concave
        app.grasses.clear();
        app.paths.add(new Path(20, 0, null));
        app.cementTiles.add(new Cement(0, 20, null));
        app.grasses.add(new Grass(20, 40, null));

        ArrayList<Tile> clingTiles = bettle.getClingTiles(app);
        assertEquals(3, clingTiles.size());
        bettle.destroyGrass(app);
        bettle.diagonal = Direction.TopLeft;
        bettle.reflectDirection(app);
        assertEquals(Direction.BottomRight, bettle.diagonal);
        assertTrue(app.paths.get(0).isRed);
        app.paths.clear();
        app.cementTiles.clear();

        // reflect plain
        app.cementTiles.add(new Cement(20, 0, null));
        bettle.diagonal = Direction.TopLeft;
        bettle.reflectDirection(app);
        assertEquals(Direction.BottomLeft, bettle.diagonal);
        bettle.diagonal = Direction.TopRight;
        bettle.reflectDirection(app);
        assertEquals(Direction.BottomRight, bettle.diagonal);
        app.cementTiles.clear();

        app.cementTiles.add(new Cement(40, 20, null));
        bettle.reflectDirection(app);
        assertEquals(Direction.BottomLeft, bettle.diagonal);
        bettle.diagonal = Direction.TopRight;
        bettle.reflectDirection(app);
        assertEquals(Direction.TopLeft, bettle.diagonal);
        app.cementTiles.clear();

        app.cementTiles.add(new Cement(0, 20, null));
        bettle.diagonal = Direction.TopLeft;
        bettle.reflectDirection(app);
        assertEquals(Direction.TopRight, bettle.diagonal);
        bettle.diagonal = Direction.BottomLeft;
        bettle.reflectDirection(app);
        assertEquals(Direction.BottomRight, bettle.diagonal);
        app.cementTiles.clear();

        app.cementTiles.add(new Cement(20, 40, null));
        bettle.diagonal = Direction.BottomLeft;
        bettle.reflectDirection(app);
        bettle.diagonal = Direction.TopLeft;
        bettle.diagonal = Direction.BottomRight;
        bettle.reflectDirection(app);
        bettle.diagonal = Direction.TopRight;
    }

    @Test
    // test collect powerup, spawn, valid and invalid
    public void testPowerup() {
        App app = new App();
        app.noLoop(); //optional
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

        app.randomInterval = 1000;
        app.unCollectedPowerup = new Powerup(app);
        assertEquals(0, app.unCollectedPowerup.type);

        app.player.x = app.unCollectedPowerup.x;
        app.player.y = app.unCollectedPowerup.y;
        app.unCollectedPowerup.checkCollected(app);
        assertTrue(app.enemies.get(0).isFrozen);
        assertNull(app.unCollectedPowerup);
        assertNotNull(app.collectedPowerup);
        app.showText();

        app.randomInterval = 1001;
        app.unCollectedPowerup = new Powerup(app);
        assertEquals(1, app.unCollectedPowerup.type);
        app.player.x = app.unCollectedPowerup.x;
        app.player.y = app.unCollectedPowerup.y;
        app.unCollectedPowerup.checkCollected(app);
        app.showText();
        assertEquals(6, app.propagationSpeed);

        app.unCollectedPowerup = new Powerup(app);
        app.unCollectedPowerup.type = 0;
        app.player.x = app.unCollectedPowerup.x;
        app.player.y = app.unCollectedPowerup.y;
        app.unCollectedPowerup.checkCollected(app);
        assertEquals(3, app.propagationSpeed);

        app.unCollectedPowerup = new Powerup(app);
        app.grasses.add(new Grass(app.unCollectedPowerup.x, app.unCollectedPowerup.y, null));
        app.unCollectedPowerup.checkCollected(app);
        assertNull(app.unCollectedPowerup);
        app.grasses.clear();

            for (int i = 0; i < 64; i++)
                for (int j = 0; j< 30; j++)
                    app.cementTiles.add(new Cement(20*i, 80 + 20*j, null));
        app.unCollectedPowerup = new Powerup(app);
        app.enemies.get(0).randomSpawn(app);
        app.cementTiles.clear();
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
        assertEquals(1, willRed.size());

        p.propagateRed(app);
        assertTrue(p2.isRed);
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
