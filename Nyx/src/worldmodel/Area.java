package worldmodel;

public class Area extends PlaneElement {
    public int width, height;

    public Area(int posX, int posY, String data) {
        super(posX, posY, "a", data);
    }

    @Override
    public void declSubPlane() {
        subPlane = new Tile[width][height];
    }

    PlaneElement initSubPlane(int x, int y, String data) {
        return subPlane[x][y] = new Tile(x, y, data);
    }
}