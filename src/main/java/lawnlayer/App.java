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

    public int currentLevel;
    public int maxLevel;

    public ArrayList<Cement> cementTiles;

    public Player player;
    public ArrayList<Enemy> enemies;

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

        this.currentLevel = 0;
        this.maxLevel = this.loadJSONObject(this.configPath).getJSONArray("levels").size();

        // create cement tiles
        this.cementTiles = createCement(this);

        // Initialise characters
        this.player = createPlayer(this);
        this.enemies = createEnemies(this);

        // Finish
        this.currentLevel++;
    }

    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        background(244, 164, 96); // Sandy Brown

        //tick
        this.player.tick();

        for (Enemy enemy: this.enemies)
            enemy.tick();

        // draw
        for (Cement cement: this.cementTiles)
            cement.draw(this);

        this.player.draw(this);

        for (Enemy enemy: this.enemies)
            enemy.draw(this);

    }

    public void keyPressed() {
        if (this.keyCode == 37 )
            this.player.leftMoving = true;
        if (this.keyCode == 38)
            this.player.upMoving = true;
        if (this.keyCode == 39)
            this.player.rightMoving = true;
        if (this.keyCode == 40)
            this.player.downMoving = true;
    }

    public void keyReleased() {
        if (this.keyCode == 37) {
            this.player.leftMoving = false;
            if (this.player.x % SPRITESIZE != 0)
                this.player.slideDirection = Direction.Left;
        }
        if (this.keyCode == 38) {
            this.player.upMoving = false;
            if (this.player.y % SPRITESIZE != 0)
                this.player.slideDirection = Direction.Up;
        }
        if (this.keyCode == 39) {
            this.player.rightMoving = false;
            if (this.player.x % SPRITESIZE != 0)
                this.player.slideDirection = Direction.Right;
        }
        if (this.keyCode == 40) {
            this.player.downMoving = false;
            if (this.player.y % SPRITESIZE != 0)
                this.player.slideDirection = Direction.Down;
        }
    }

    public static ArrayList<Cement> createCement(App app) {
        ArrayList<Cement> cementTiles = new ArrayList<Cement>();

        JSONArray levels = app.loadJSONObject(app.configPath).getJSONArray("levels");
        boolean[][] grid = readMap(levels.getJSONObject(app.currentLevel).getString("outlay"));

        checkMapValid(grid);

        for (int row = 0; row < grid.length; row++)
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column])
                    cementTiles.add(new Cement(column * SPRITESIZE, row * SPRITESIZE + TOPBAR, app.concrete));
            }
        return cementTiles;
    }

    public static boolean[][] readMap(String path) {
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

    public static boolean checkMapValid(boolean[][] grid) throws Error{
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

    public static Player createPlayer(App app) {
        Player player = new Player(0, TOPBAR, app.ball);
        int lives = app.loadJSONObject(app.configPath).getInt("lives");
        player.setLives(lives);
        return player;
    }

    public static ArrayList<Enemy> createEnemies(App app) {
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();

        JSONArray levels = app.loadJSONObject(app.configPath).getJSONArray("levels");
        JSONArray enemiesConfig = levels.getJSONObject(app.currentLevel).getJSONArray("enemies");
        int enemiesCount = enemiesConfig.size();

        for (int i = 0; i < enemiesCount; i++) {
            JSONObject currentEnemy = enemiesConfig.getJSONObject(i);
            Enemy enemy = new Enemy(0, 0, null);

            if (currentEnemy.getInt("type") == 0)
                enemy.sprite = app.worm;
            else if(currentEnemy.getInt("type") == 1)
                enemy.sprite = app.beetle;

            if (currentEnemy.getString("spawn").equals("random"))
                enemy.randomSpawn(app);
            else {
                enemy.x = 20 + 20 * i;
                enemy.y = 100 + 20 * i;
            }

            enemies.add(enemy);
        }

        return enemies;
    }

    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
