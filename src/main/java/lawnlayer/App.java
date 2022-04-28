package lawnlayer;

import processing.core.PApplet;
import processing.core.PFont;
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
    public PFont f;

    public boolean gameOver;
    public boolean gameWin;
    public int currentLevel;
    public double goal;
    public double progress;
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
    public int powerupDurationTimer;
    public int powerupSpawnTimer;
    public int randomInterval;
    public int propagationSpeed;

    public App() {
        this.configPath = "config.json";
//        this.configPath = "config_invalidmap.json";
//        this.configPath = "config_not_random_spawn.json";
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
        f = createFont("Times New Roman",36,true);

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
        this.propagationSpeed = 3;
        this.currentLevel++;
    }

    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        background(244, 164, 96); // Sandy Brown

        if (!this.gameOver && !this.gameWin) {
            //tick
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

            if (this.collectedPowerup != null)
                this.collectedPowerup.draw(this);
        }

        this.winConditionCheck();
        this.showText();
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

        if (this.keyCode == 80 && (this.gameOver || this.gameWin)) {
            this.gameOver = false;
            this.gameWin = false;
            this.player.moveOrigin(this);
            this.setup();
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
        this.goal = 100 * levels.getJSONObject(this.currentLevel).getDouble("goal");
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
        if (this.propagateTimer >= this.propagationSpeed) {
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
        // 5-8s get interval
        if (this.randomInterval == 0)
            this.randomInterval = 300 + (int)(Math.random()*180);

        if (this.unCollectedPowerup == null)
            this.powerupSpawnTimer++;
        else
            this.unCollectedPowerup.checkCollected(this);

        if(this.powerupSpawnTimer == this.randomInterval && this.unCollectedPowerup == null) {
            this.unCollectedPowerup = new Powerup(this);
            randomInterval = 0;
        }

        if (collectedPowerup != null) {
            this.powerupDurationTimer++;
            this.collectedPowerup.validPowerup(this);
        }

        if (this.powerupDurationTimer == 600 && this.collectedPowerup != null) {
            this.collectedPowerup.invalidPowerup(this);
            this.collectedPowerup = null;
            this.powerupDurationTimer = 0;
        }

    }

    public ArrayList<Path> getCurrentRed() {
        ArrayList<Path> redPaths = new ArrayList<Path>();
        for(Path path: this.paths)
            if(path.isRed)
                redPaths.add(path);
        return redPaths;
    }

    public void showText() {
        textFont(f);
        fill(0);

        String lives = "Lives: " + this.player.lives;
        String progress = (int)this.progress + "%/" + (int)(this.goal) + "%";
        String level = "Level " + this.currentLevel;
        String remainingTime = "Remaining Time: " + (100 - this.powerupDurationTimer/6)/10.0;
        String power0 = "IT IS YOUR TIME!!!";
        String power1 = "Catch Me If You Can!";
        String win = "YOU WIN!";
        String over = "GAME OVER";
        String restart = "Press P Restart";

        if (!this.gameOver && !this.gameWin) {
            text(lives,20,40);
            text(progress,1140,40);
            textFont(f, 24);
            text(level,1190,70);

            if (this.collectedPowerup != null) {
                text(remainingTime,50,70);
                if (this.collectedPowerup.type == 0)
                    text(power0,300,70);
                if (this.collectedPowerup.type == 1)
                    text(power1,300,70);
            }
        }
        else {
            fill(255);
            textFont(f, 36);
            text(restart,520,400);
            textFont(f, 72);
            if(this.gameWin)
                text(win,460,360);
            else if(this.gameOver)
                text(over,420,360);
        }
    }

    public void winConditionCheck() {
        this.progress = Math.round(100 * (float)this.grasses.size() / (32*64 - this.cementTiles.size()));

        if (this.player.lives == 0 && !this.gameOver) {
            this.gameOver = true;
            System.out.println("over");
        }

        if (this.progress >= this.goal && !this.gameWin) {
            if (this.currentLevel >= this.maxLevel) {
                this.gameWin = true;
                System.out.println("win");
                return;
            }
            this.player.moveOrigin(this);
            this.enemies = this.createEnemies();
            this.cementTiles = this.createCement();
            this.grasses.clear();
            this.currentLevel++;
        }
    }
    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
