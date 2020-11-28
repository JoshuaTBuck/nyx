package worldmodel;

import myHelperClasses.Properties;

public abstract class PlaneElement {
    Properties propertiesToSave;
    public int posX, posY;

    public PlaneElement[][] subPlane;
    String marker;

    private boolean changedSinceLastSaved = true;
    String propertyMarker = "-";
    String elementMarker = ":";
    String properties;
    private String lastSavedData = "";

    public PlaneElement(int posX, int posY, String marker, String data) {
        propertiesToSave = new Properties("width", "height");
        this.posX = posX;
        this.posY = posY;
        this.marker = marker;
        initPlane(data);
    }

    abstract void declSubPlane();

    abstract PlaneElement initSubPlane(int x, int y, String data);

    private void initPlane(String data) {
        // Incomming data is expected to have the Form: <props><subPlaneData>
        properties = data.split(propertyMarker, 2)[0];
        // init props
        propertiesToSave.setFields(this, properties);
        // subPlaneData is a row of subElementData-String dividet by markers
        if (data.split(propertyMarker, 2).length > 1) {
            String[] subPlaneData = data.split(propertyMarker, 2)[1].split("<" + marker + ">");
            // init subPlane
            declSubPlane();

            int counter = 0;
            // subElementData has the Form: <numElements><subElement>
            String[] subElementData = subPlaneData[counter].split(elementMarker, 2);
            int subElementsCreated = 0;

            for (int y = 0; y < subPlane[0].length; y++) {
                for (int x = 0; x < subPlane.length; x++) {
                    if (subElementsCreated == Integer.parseInt(subElementData[0])) {
                        counter++;
                        subElementData = subPlaneData[counter].split(elementMarker, 2);
                        subElementsCreated = 0;
                    }
                    subPlane[x][y] = initSubPlane(x, y, subElementData[1]);
                    subElementsCreated++;
                }
            }
        }
    }

    public String getSaveData() {
        if (changedSinceLastSaved) {
            int elementsInRow = 0;
            String saveString = propertiesToSave.getFields(this) + propertyMarker;
            String lastSubPlaneData = "";

            for (int y = 0; y < subPlane[0].length; y++) {
                for (int x = 0; x < subPlane.length; x++) {
                    String currentSubPlaneData = subPlane[x][y].getSaveData();
                    if (lastSubPlaneData.equals(currentSubPlaneData) || lastSubPlaneData.equals("")) {
                        elementsInRow++;
                    } else {
                        saveString += elementsInRow + elementMarker + lastSubPlaneData + "<" + marker + ">";
                        elementsInRow = 1;
                    }
                    lastSubPlaneData = currentSubPlaneData;
                }
            }
            saveString += elementsInRow + elementMarker + lastSubPlaneData;

            changedSinceLastSaved = false;
            lastSavedData = saveString;
            return saveString;
        } else {
            return lastSavedData;
        }
    }
}