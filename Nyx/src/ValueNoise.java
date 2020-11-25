import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class ValueNoise {
    public static float[][] generateNoiseMap(int mapWidth, int mapHeight, int distance) {
        // int calcDist = (int) Math.pow(2, rawDist);
        int calcWidth = (int) (Math.ceil(mapWidth / (float) distance) * distance) + 1;
        int calcHeight = (int) (Math.ceil(mapHeight / (float) distance) * distance) + 1;
        float[][] noiseMap = new float[calcWidth][calcHeight];
        // maxAltitude only has a strong influence when the values are very low (approx.
        // 5); therefore this variable cannot be calibrated
        int maxAltitude = 100;

        // Number of starting Points (width * height)
        int numPointsInHeight = (int) Math.ceil(noiseMap[0].length / (float) distance);
        int numPointsInWidth = (int) Math.ceil(noiseMap.length / (float) distance);

        // Create starting Points on Map
        int minPoint = maxAltitude - 1, maxPoint = 0;
        for (int y = 0; y < numPointsInHeight; y++) {
            for (int x = 0; x < numPointsInWidth; x++) {
                int newNum = new Random().nextInt(maxAltitude);

                if (newNum < minPoint) {
                    minPoint = newNum;
                }
                if (newNum > maxPoint) {
                    maxPoint = newNum;
                }
                noiseMap[x * distance][y * distance] = newNum;
            }
        }

        // Normalize the noiseMap
        for (int y = 0; y < noiseMap[0].length; y++) {
            for (int x = 0; x < noiseMap.length; x++) {
                noiseMap[x][y] /= maxPoint;
            }
        }

        noiseMap = interpolateNoiseMap(noiseMap, numPointsInWidth, numPointsInHeight, distance);
        noiseMap = cropNoiseMap(noiseMap, mapWidth, mapHeight);

        return noiseMap;
    }

    public static float[][] combineNoiseMaps(float[][]... noiseMaps) {
        float[][] newNoiseMap = new float[noiseMaps[0].length][noiseMaps[0][0].length];

        for (int y = 0; y < noiseMaps[0][0].length; y++) {
            for (int x = 0; x < noiseMaps[0].length; x++) {
                for (int i = 0; i < noiseMaps.length; i++) {
                    newNoiseMap[x][y] += noiseMaps[i][x][y];
                }
                newNoiseMap[x][y] /= noiseMaps.length;
            }
        }

        return newNoiseMap;
    }

    private static float[][] interpolateNoiseMap(float[][] noise, int numPointsInWidth, int numPointsInHeight,
            int calcDist) {
        // Interpolate along the x axis
        for (int y = 0; y < numPointsInHeight; y++) {
            for (int x = 0; x < numPointsInWidth; x++) {
                if (x < numPointsInWidth - 1) {
                    float dif = noise[x * calcDist][y * calcDist] - noise[(x + 1) * calcDist][y * calcDist];
                    for (int i = 0; i < calcDist; i++) {
                        noise[x * calcDist + i][y * calcDist] = noise[x * calcDist][y * calcDist]
                                - (dif / (calcDist)) * i;
                    }
                }
            }
        }

        // Interpolate along the y axis
        for (int y = 0; y < numPointsInHeight; y++) {
            for (int x = 0; x < noise.length; x++) {
                if (y < numPointsInHeight - 1) {
                    float dif = noise[x][y * calcDist] - noise[x][(y + 1) * calcDist];
                    for (int i = 0; i < calcDist; i++) {
                        noise[x][y * calcDist + i] = noise[x][y * calcDist] - (dif / (calcDist)) * i;
                    }
                }
            }
        }

        return noise;
    }

    private static float[][] cropNoiseMap(float[][] noise, int rawWidth, int rawHeight) {
        float[][] croppedNoiseMap = new float[rawWidth][rawHeight];
        for (int y = 0; y < rawHeight; y++) {
            for (int x = 0; x < rawWidth; x++) {
                croppedNoiseMap[x][y] = noise[x][y];
            }
        }
        return croppedNoiseMap;
    }

    public static void createNoiseImage(float[][] noise) {
        BufferedImage noiseImage = new BufferedImage(noise.length, noise[0].length, BufferedImage.TYPE_INT_RGB);

        // transfers the noise-map into a noise-image
        for (int y = 0; y < noise[0].length; y++) {
            for (int x = 0; x < noise.length; x++) {
                // create a gray pixel on the image
                int c = (int) (Color.white.getRed() * noise[x][y]);
                noiseImage.setRGB(x, y, new Color(c, c, c).getRGB());
            }
        }

        try {
            ImageIO.write(noiseImage, "png", new File("Nyx/gfx/noiseImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}