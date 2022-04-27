package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class App extends PApplet {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int TOPBAR = 80;

    public static final int FPS = 60;

    public String configPath;
	
	public PImage grass;
    public PImage concrete;
    public PImage worm;
    public PImage beetle;
    public PImage ball;
    public PImage green;
    public PImage red;
    public PImage powerup_0;
    public PImage powerup_1;

    public boolean gameOver;
    public int currentLevel;
    public double goal;
    public int maxLevel;

    public ArrayList<Cement> cementTiles;
    public ArrayList<Grass> grasses;
    public ArrayList<Path> paths;
    public ArrayList<Path> currentRed;

    public Player player;
    public ArrayList<Enemy> enemies;

    public Powerup collectedPowerup;
    public Powerup unCollectedPowerup;

    public int propagateTimer;
    public int powerDurationTimer;
    public int powerupSpawnTimer;
    public int randomInterval;

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);

        // Load images during setup
		this.grass = loadImage(this.getClass().getResource("grass.png").getPath());
        this.concrete = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
        this.worm = loadImage(this.getClass().getResource("worm.png").getPath());
        this.beetle = loadImage(this.getClass().getResource("beetle.png").getPath());
        this.ball = loadImage(this.getClass().getResource("ball.png").getPath());
        this.green = loadImage(this.getClass().getResource("green.png").getPath());
        this.red = loadImage(this.getClass().getResource("red.png").getPath());
        this.powerup_0 = loadImage(this.getClass().getResource("powerup_0.png").getPath());
        this.powerup_1 = loadImage(this.getClass().getResource("powerup_1.png").getPath());

        this.currentLevel = 0;
        this.maxLevel = this.loadJSONObject(this.configPath).getJSONArray("levels").size();

        // create cement tiles
        this.cementTiles = this.createCement();
        this.grasses = new ArrayList<Grass>();
        this.paths = new ArrayList<Path>();
        this.currentRed = new ArrayList<Path>();

        // Initialise characters
        this.player = this.createPlayer();
        this.enemies = this.createEnemies();

        // Finish
        this.currentLevel++;
    }

    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        background(244, 164, 96); // Sandy Brown

        if (this.gameOver)
            System.out.println("gameover");

        //tick
        if (!this.gameOver) {
            this.propagateTimerIncrease();
            this.powerupEvent();

            this.player.createPath(this);
            this.player.updatePositionFlag(this);
            this.player.checkLoseOneLife(this);
            this.player.tick();

            for (Enemy enemy: this.enemies) {
                enemy.reflectDirection(this);
                if (enemy.type == 1)
                     enemy.destroyGrass(this);
                if (!enemy.isFrozen)
                    enemy.tick();
            }

            for (Path path: this.currentRed)
                path.propagateRed(this);
        }

        // draw
        for (Cement cement: this.cementTiles)
            cement.draw(this);

        for (Grass grass: this.grasses)
            grass.draw(this);

        for (Path path: this.paths)
            path.draw(this);

        this.player.draw(this);

        for (Enemy enemy: this.enemies)
            enemy.draw(this);

        if (this.unCollectedPowerup != null)
            this.unCollectedPowerup.draw(this);


        // test
//        System.out.println(this.enemies.get(0).x + ", "+ this.enemies.get(0).y);
//        for (Cement cement: this.cementTiles)
//            if (this.player.checkCling(cement))
//                System.out.println("yes");
//        System.out.println(this.player.slideDirection + " "+ this.player.upDownDirection+ " "+this.player.x+" "+this.player.y);
//        System.out.println(this.player.onCement+ " "+this.player.centerCement+" "+this.player.soilSlideDirection + this.player.x);
//        System.out.println(this.player.x+ " "+this.player.y);
//        System.out.println(this.grasses.size());
//        System.out.println(this.player.existsTile(this, 0, 100));
//        if (this.paths.size() != 0)
//            this.player.floodFill(this, this.paths.get(0));
//        System.out.println(this.grasses.size());
//        System.out.println(timer);
//        for (Path path: this.paths)
//            if(path.isRed)
//                System.out.println("1");
//        int temp = 0;
//        for (Path path: this.paths) {
//            if(path.isRed)
//                temp++;
//        }
//        System.out.println(temp);
//        System.out.println(this.grasses.size());
//        System.out.println(this.player.loseOneLife(this));
//        System.out.println(this.player.lives);
//        System.out.println(this.grasses.size());
//        System.out.println(this.player.centerCement +" "+this.player.centerGrass);
//        System.out.println(this.goal);
//        if (this.powerupTimer % 60 == 0)
//            System.out.println(this.powerupTimer);
//        System.out.println(this.randomInterval + " "+this.powerupTimer);
//        System.out.println(this.player.duringPowerup);
//        System.out.println(this.powerSpawnTimer);

    }

    public void keyPressed() {
        if (this.keyCode == 37 ) {
            this.player.leftMoving = true;
            this.player.leftRightDirection = Direction.Left;

            if(this.player.slideDirection == Direction.Up || this.player.slideDirection == Direction.Down)
                this.player.turnDirection = Direction.Left;
        }

        if (this.keyCode == 39 ) {
            this.player.rightMoving = true;
            this.player.leftRightDirection = Direction.Right;

            if(this.player.slideDirection == Direction.Up || this.player.slideDirection == Direction.Down)
                this.player.turnDirection = Direction.Right;
        }

        if (this.keyCode == 38) {
            this.player.upMoving = true;
            this.player.upDownDirection = Direction.Up;

            if(this.player.slideDirection == Direction.Left || this.player.slideDirection == Direction.Right)
                this.player.turnDirection = Direction.Up;
        }

        if (this.keyCode == 40) {
            this.player.downMoving = true;
            this.player.upDownDirection = Direction.Down;
            if(this.player.slideDirection == Direction.Left || this.player.slideDirection == Direction.Right)
                this.player.turnDirection = Direction.Down;
        }
    }

    public void keyReleased() {
        if (this.keyCode == 37) {
            this.player.leftMoving = false;
        }
        if (this.keyCode == 39) {
            this.player.rightMoving = false;
        }
        if (this.keyCode == 38) {
            this.player.upMoving = false;
        }
        if (this.keyCode == 40) {
            this.player.downMoving = false;
        }
    }

    public ArrayList<Cement> createCement() {
        ArrayList<Cement> cementTiles = new ArrayList<Cement>();

        JSONArray levels = this.loadJSONObject(this.configPath).getJSONArray("levels");
        this.goal = levels.getJSONObject(this.currentLevel).getDouble("goal");
        boolean[][] grid = readMap(levels.getJSONObject(this.currentLevel).getString("outlay"));

        this.checkMapValid(grid);

        for (int row = 0; row < grid.length; row++)
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column])
                    cementTiles.add(new Cement(column * SPRITESIZE, row * SPRITESIZE + TOPBAR, this.concrete));
            }
        return cementTiles;
    }

    public boolean[][] readMap(String path) {
        File f = new File(path);
        Scanner scan;
        boolean[][] grid = new boolean[32][64];
        int row = -1;

        try {
            scan = new Scanner(f);
        }catch (FileNotFoundException e) {
            return null;
        }

        while (scan.hasNextLine() && row < 32) {
            String line = scan.nextLine();
            row ++;
            for (int column = 0; column < line.length(); column++) {
                if (line.charAt(column) == 'X')
                    grid[row][column] = true;
            }
        }
        return grid;
    }

    public boolean checkMapValid(boolean[][] grid) throws Error{
        if (grid == null)
            throw new Error("Invalid Map.");

        for (int i = 0; i < 64; i++) {
            if (!grid[0][i] || !grid[31][i])
                throw new Error("Invalid Map.");
        }

        for (int i = 0; i < 32; i++) {
            if (!grid[i][0] || !grid[i][63])
                throw new Error("Invalid Map.");
        }
        return true;
    }

    public Player createPlayer() {
        Player player = new Player(0, TOPBAR, this.ball);
        int lives = this.loadJSONObject(this.configPath).getInt("lives");
        player.setLives(lives);
        return player;
    }

    public ArrayList<Enemy> createEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();

        JSONArray levels = this.loadJSONObject(this.configPath).getJSONArray("levels");
        JSONArray enemiesConfig = levels.getJSONObject(this.currentLevel).getJSONArray("enemies");
        int enemiesCount = enemiesConfig.size();

        for (int i = 0; i < enemiesCount; i++) {
            JSONObject currentEnemy = enemiesConfig.getJSONObject(i);
            Enemy enemy;

            if (currentEnemy.getInt("type") == 0)
                enemy = new Enemy(0, 0, this.worm);
            else if(currentEnemy.getInt("type") == 1)
                enemy = new Bettle(0, 0, this.beetle);
            //can register new type enemy here
            else
                enemy = new Enemy(0, 0, this.worm);

            if (currentEnemy.getString("spawn").equals("random"))
                enemy.randomSpawn(this);
            else {
                String[] position = currentEnemy.getString("spawn").split(",");
                enemy.x = SPRITESIZE * (Integer.parseInt(position[1]) - 1);
                enemy.y = TOPBAR + SPRITESIZE * (Integer.parseInt(position[0]) - 1);
            }
            enemies.add(enemy);
        }
        return enemies;
    }

    public void propagateTimerIncrease() {
        if (this.propagateTimer == 3) {
            this.currentRed = this.getCurrentRed();
            this.propagateTimer = 0;
        }

        if (this.paths.size() == 0) {
            this.currentRed.clear();
            this.propagateTimer = 0;
            return;
        }

        for (Path path: this.paths)
            if (path.isRed) {
                this.propagateTimer++;
                return;
            }
    }

    public void powerupEvent() {
        // 5-10s get interval
        if (this.randomInterval == 0)
            this.randomInterval = 300 + (int)(Math.random()*300);

        if (this.unCollectedPowerup == null)
            this.powerupSpawnTimer++;
        else
            this.unCollectedPowerup.checkCollected(this);

        if(this.powerupSpawnTimer == this.randomInterval && this.unCollectedPowerup == null) {
            this.unCollectedPowerup = new Powerup(this);
            randomInterval = 0;
        }

        if (collectedPowerup != null) {
            this.powerDurationTimer++;
        }

        if (this.powerDurationTimer == 600) {
            this.collectedPowerup = null;
        }

    }

    public ArrayList<Path> getCurrentRed() {
        ArrayList<Path> redPaths = new ArrayList<Path>();
        for(Path path: this.paths)
            if(path.isRed)
                redPaths.add(path);
        return redPaths;
    }

    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
