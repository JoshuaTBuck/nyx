package worldmodel;

public class Segment extends PlaneElement {
    public int width, height;

    public Segment(int posX, int posY, String data) {
        super(posX, posY, "s", data);
    }

    @Override
    public void declSubPlane() {
        subPlane = new Area[width][height];
    }

    PlaneElement initSubPlane(int x, int y, String data) {
        return subPlane[x][y] = new Area(x, y, data);
    }
}