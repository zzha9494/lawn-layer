package lawnlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class temp {
    public static void main(String[] args) {
        String path = "level1.txt";
        boolean result = _check_map_valid(_read_map(path));
        System.out.println(result);



    }

    public static boolean[][] _read_map(String path) {
        File f = new File(path);
        Scanner scan = null;
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

    public static boolean _check_map_valid(boolean[][] grid) {
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

}
