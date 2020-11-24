import java.util.Random;

public class World {
    int[][] ground = new int[8][8];
    Random r = new Random();

    public World() {
        generateNewWorld();
    }

    public World(int size) {
        ground = new int[size][size];
        generateNewWorld();
    }

    public void generateNewWorld() {
        for (int y = 0; y < ground[0].length; y++) {
            for (int x = 0; x < ground.length; x++) {
                ground[x][y] = r.nextInt(5);
            }
        }
    }

    public void printData() {
        for (int y = -1; y < ground[0].length; y++) {
            for (int x = -1; x < ground.length; x++) {
                if (x < 0 && y < 0) {
                    System.out.print("x/y");
                } else if (x < 0) {
                    System.out.print("(" + y + ")");
                } else if (y < 0) {
                    System.out.print("(" + x + ")");
                } else {
                    System.out.print(" " + ground[x][y] + " ");
                }
            }
            System.out.println();
        }
    }
}
