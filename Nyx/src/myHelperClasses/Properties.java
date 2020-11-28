package myHelperClasses;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Properties {

    private ArrayList<String> whiteList = new ArrayList<>();

    public Properties() {
    }

    public Properties(String... whiteList) {
        for (String listElement : whiteList) {
            whiteList(listElement);
        }
    }

    public void whiteList(String fieldName) {
        boolean found = false;

        for (String listElement : whiteList) {
            if (listElement.equals(fieldName)) {
                found = true;
                break;
            }
        }
        if (!found) {
            whiteList.add(fieldName);
        }
    }

    public String getFields(Object o) {
        String data = "";
        for (Field f : o.getClass().getDeclaredFields()) {
            try {
                for (String listElement : whiteList) {
                    if (listElement.equals(f.getName())) {
                        data += f.getType() + ":" + f.getName() + ":" + f.get(o) + ";";
                    }
                }
            } catch (IllegalStateException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public void setFields(Object o, String data) {
        setFields(o, data, false);
    }

    public void setFields(Object o, String data, boolean showWarnings) {
        for (String field : data.split(";")) {
            String[] splitField = field.split(":", 3);
            boolean found = false;
            for (String listElement : whiteList) {
                if (listElement.equals(splitField[1])) {
                    found = true;
                    switch (splitField[0]) {
                        case "boolean":
                            setBoolean(o, splitField[1], splitField[2]);
                            break;
                        case "short":
                            setShort(o, splitField[1], splitField[2]);
                            break;
                        case "int":
                            setInteger(o, splitField[1], splitField[2]);
                            break;
                        case "long":
                            setLong(o, splitField[1], splitField[2]);
                            break;
                        case "double":
                            setDouble(o, splitField[1], splitField[2]);
                            break;
                        case "float":
                            setFloat(o, splitField[1], splitField[2]);
                            break;
                        case "class java.lang.String":
                            setString(o, splitField[1], splitField[2]);
                            break;
                        default:
                            System.out.println("Unknown Type " + field);
                            break;
                    }
                }
            }
            if (!found && showWarnings) {
                System.out.println("WARNING: A loaded Element was not specified on the WhiteList -> "+splitField[1]);
            }
        }
    }

    private void setBoolean(Object o, String name, String value) {
        Field field;
        try {
            field = o.getClass().getDeclaredField(name);
            field.setBoolean(o, Boolean.parseBoolean(value));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setShort(Object o, String name, String value) {
        try {
            Field field = o.getClass().getDeclaredField(name);
            field.setShort(o, Short.parseShort(value));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setInteger(Object o, String name, String value) {
        try {
            Field field = o.getClass().getDeclaredField(name);
            field.setInt(o, Integer.parseInt(value));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setLong(Object o, String name, String value) {
        try {
            Field field = o.getClass().getDeclaredField(name);
            field.setLong(o, Long.parseLong(value));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setDouble(Object o, String name, String value) {
        try {
            Field field = o.getClass().getDeclaredField(name);
            field.setDouble(o, Double.parseDouble(value));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setFloat(Object o, String name, String value) {
        try {
            Field field = o.getClass().getDeclaredField(name);
            field.setFloat(o, Float.parseFloat(value));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setString(Object o, String name, String value) {
        try {
            Field field = o.getClass().getDeclaredField(name);
            field.set(o, value);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void scanFields(Object o) {
        System.out.println("Fields found for " + o.getClass().getSimpleName() + ":");
        int fieldsFound = 0;
        for (Field f : o.getClass().getDeclaredFields()) {
            try {
                for (String string : whiteList) {
                    if (string.equals(f.getName())) {
                        System.out.println(f.getType() + " " + f.getName() + " " + f.get(o) + ";");
                        fieldsFound++;
                    }
                }
            } catch (IllegalStateException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (whiteList.size() != fieldsFound) {
            System.out.println("----------------------");
            System.out.println("WARNING: The specified WhiteList does not match the Fields found");
            System.out.println("WhiteList length: " + whiteList.size());
            System.out.println("Fields found:     " + fieldsFound);
        } else {
            System.out.println("----------------------");
            System.out.println("Total Fields found: " + fieldsFound);
        }
    }
}