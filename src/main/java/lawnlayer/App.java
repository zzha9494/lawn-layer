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
    }
	
    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        background(244, 164, 96); // Sandy Brown

        //tick
        this.player.tick();

        // draw
        for (Cement cement: this.cementTiles)
            cement.draw(this);

        this.player.draw(this);

    }

    public void keyPressed() {
        if (this.keyCode == 37)
            this.player.moveLeft = true;
        if (this.keyCode == 38)
            this.player.moveUp = true;
        if (this.keyCode == 39)
            this.player.moveRight = true;
        if (this.keyCode == 40)
            this.player.moveDown = true;
    }

    public void keyReleased() {
        if (this.keyCode == 37)
            this.player.moveLeft = false;
        if (this.keyCode == 38)
            this.player.moveUp = false;
        if (this.keyCode == 39)
            this.player.moveRight = false;
        if (this.keyCode == 40)
            this.player.moveDown = false;
    }

    public static ArrayList<Cement> createCement(App app) {
        ArrayList<Cement> cementTiles = new ArrayList<Cement>();
        JSONArray levels = app.loadJSONObject(app.configPath).getJSONArray("levels");
        boolean[][] grid = readMap(levels.getJSONObject(app.currentLevel++).getString("outlay"));

        if (grid == null)
            return cementTiles;

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

    public static boolean checkMapValid(boolean[][] grid) {
        for (int i = 0; i < 64; i++) {
            if (!grid[0][i] || !grid[31][i])
                return false;
        }

        for (int i = 0; i < 32; i++) {
            if (!grid[i][0] || !grid[i][63])
                return false;
        }
        return true;
    }

    public static Player createPlayer(App app) {
        Player player = new Player(0, TOPBAR, app.ball);
        int lives = app.loadJSONObject(app.configPath).getInt("lives");
        player.setLives(lives);
        return player;
    }

    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
