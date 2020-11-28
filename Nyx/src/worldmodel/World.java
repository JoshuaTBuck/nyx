package worldmodel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class World extends PlaneElement {
    public int width, height;

    public World(String data) {
        super(0, 0, "w", data);
        saveWorld();
    }

    public World() {
        super(0, 0, "w", readWorldData());
    }

    @Override
    public void declSubPlane() {
        subPlane = new Segment[width][height];
    }

    PlaneElement initSubPlane(int x, int y, String data) {
        return subPlane[x][y] = new Segment(x, y, data);
    }

    public void saveWorld() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Nyx/data/worldData.txt"));
            writer.append(getSaveData());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readWorldData() {
        String data = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Nyx/data/worldData.txt"));
            data = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}