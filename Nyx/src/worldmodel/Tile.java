package worldmodel;

public class Tile extends PlaneElement {

    public String biomeKey = "";
    public int width, height;

    public Tile(int posX, int posY, String data) {
        super(posX, posY, "t", data);
        propertiesToSave.whiteList("biomeKey");
        propertiesToSave.setFields(this, properties, true);
    }

    @Override
    public void declSubPlane() {
    }

    PlaneElement initSubPlane(int x, int y, String data) {
        return null;
    }

    public String getSaveData() {
        return propertiesToSave.getFields(this);
    }
}