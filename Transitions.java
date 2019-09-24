import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Transitions {
    private static int redMask = 0xFF0000, blueMask = 0xFF, greenMask = 0xFF00, hMask = 0xFF000000;
    private double[][] reference;
    private int width, height, keyframes;
    private double angle, feather, stepSize, weight;
    String[] paths;
    private File[] files;
    private BufferedImage[] images;
    private int[] colors;
    private int red = 0, green = 0, blue = 0, head = 0;
    Transitions(int w, int h, int k, double a, double f, String[] p) {
        width = w;
        height = h;
        keyframes = k;
        angle = a; //180 = right to left, 90 = down to up
        feather = f;
        paths = new String[3]; 
        paths[0] = p[0];
        paths[1] = p[1];
        paths[2] = p[2];
        reference = new double[width][height];
        files = new File[3];
        images = new BufferedImage[3];
        colors = new int[3];
    }
    public static void main(String[] args) { //example usage
        String[] pathnames = new String[3];
        pathnames[0] = "C:\\Users\\Bella\\Documents\\Transition Hold\\I1_";
        pathnames[1] = "C:\\Users\\Bella\\Documents\\Transition Hold\\I2_";
        pathnames[2] = "C:\\Users\\Bella\\Documents\\Transition Hold\\I3_";
        int width = 1280, height = 720, keyframes = 3;
        double angle = 180, feather = 20;
        Transitions t = new Transitions(width, height, keyframes, angle, feather, pathnames);
        t.circleIrisTransit();
    }
    public void wipeTransit() {
        angle = -1.0 * ((angle * Math.PI) / 180.0);
        double dx = Math.cos(angle), dy = Math.sin(angle);
        double min = 2000;
        for(int c1 = 0; c1 < width; c1++) {
            for(int c2 = 0; c2 < height; c2++) {
                reference[c1][c2] = ((c1 + 1) * dx) + ((c2 + 1) * dy);
                if(reference[c1][c2] < min)
                    min = reference[c1][c2];
            }
        }
        for(int c1 = 0; c1 < width; c1++) {
            for(int c2 = 0; c2 < height; c2++) {
                reference[c1][c2] -= min;
            }
        }
        stepSize = Math.abs(((width * Math.abs(dx)) + 0.0 + (height * Math.abs(dy))) / (keyframes + 1.0));
        transit();
    }
    public void circleIrisTransit() {
        for(int c1 = 0; c1 < width; c1++) {
            for(int c2 = 0; c2 < height; c2++) {
                reference[c1][c2] = Math.sqrt((Math.pow((c1 + 1 - ((width / 2.0) + 0.5)), 2)) + Math.pow((c2 + 1 - ((height / 2.0) + 0.5)), 2));
            }
        }
        stepSize = Math.sqrt((Math.pow(((width / 2.0) + 0.5), 2)) + Math.pow(((height / 2.0) + 0.5), 2)) / keyframes;
        transit();
    }
    public void midsweepTransit() {
        double min = 2000, a;
        stepSize = Math.PI / keyframes;
        for(int c1 = 0; c1 < width; c1++) {
            for(int c2 = 0; c2 < height; c2++) {
                a = Math.atan((c1 - (width / 2.0)) / (c2 + 0.00000001));
                reference[c1][c2] = /*Math.PI-*/(a + (Math.PI / 2.0)); //use pi- to sweep right to left
            }
        }
        transit();
    }
    public void widthHalfWipeTransit() { //splits image into left and right (barn door effect)
        double[][] reference1 = new double[width / 2][height];
        double[][] reference2 = new double[width / 2][height];
        double angle1 = angle, angle2 = angle + 180;
        angle1 = -1.0 * ((angle1 * Math.PI) / 180.0);
        angle2 = -1.0 * ((angle2 * Math.PI) / 180.0);
        double dx1 = Math.cos(angle1), dy1 = Math.sin(angle1);
        double dx2 = Math.cos(angle2), dy2 = Math.sin(angle2);
        double min1 = 2000, min2 = 2000;
        for(int c1 = 0; c1 < width / 2; c1++) {
            for(int c2 = 0; c2 < height; c2++) {
                reference1[c1][c2] = ((c1 + 1) * dx1) + ((c2 + 1) * dy1);
                if(reference1[c1][c2] < min1)
                    min1 = reference1[c1][c2];
                reference2[c1][c2] = ((c1 + 1) * dx2) + ((c2 + 1) * dy2);
                if(reference2[c1][c2] < min2)
                    min2 = reference2[c1][c2];
            }
        }
        for(int c1 = 0; c1 < width / 2; c1++) {
            for(int c2 = 0; c2 < height; c2++) {
                reference1[c1][c2] -= min1;
                reference2[c1][c2] -= min2;
            }
        }
        for(int c1 = 0; c1 < width / 2; c1++) {
            for(int c2 = 0; c2 < height; c2++) {
                reference[c1][c2] = reference1[c1][c2];
                reference[c1 + (width / 2)][c2] = reference2[c1][c2];
            }
        }
        System.out.println(dx1 + ", " + dy1 + " and " + dx2 + ", " + dy2);
        stepSize = Math.abs(((width * Math.abs(dx1 / 2)) + 0.0 + (height * Math.abs(dy1 /2))) / (keyframes + 1.0));
        transit();
    }
    public void fadeTransit() {
        String pName1, pName2, pName3;
        for(int c = 1; c <= keyframes; c++) {
            pName1 = makeName(c, paths[0], ".jpg");
            pName2 = makeName(c, paths[1], ".jpg");
            pName3 = makeName(c, paths[2], ".png");
            try { //read in files
                images[2] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                files[0] = new File(pName1);
                images[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                images[0] = ImageIO.read(files[0]);
                files[1] = new File(pName2);
                images[1] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                images[1] = ImageIO.read(files[1]);
                for(int y = 0; y < height; y++) {
                    for(int x = 0; x < width; x++) {
                        colors[0] = images[0].getRGB(x,y);
                        colors[1] = images[1].getRGB(x,y);
                        red = ((int) Math.round(((makeRed(colors[0]) * c + makeRed(colors[1]) * (keyframes - c))) / (keyframes + 0.0))) & redMask;
                        green = ((int) Math.round(((makeGreen(colors[0]) * c + makeGreen(colors[1]) * (keyframes - c))) / (keyframes + 0.0))) & greenMask;
                        blue = ((int) Math.round(((makeBlue(colors[0]) * c + makeBlue(colors[1]) * (keyframes - c))) / (keyframes + 0.0))) & blueMask;
                        head = (makeHeader(colors[0]));
                        colors[2] = red + green + blue + hMask;
                        images[2].setRGB(x, y, colors[2]);
                    }
                }
                files[2] = new File(pName3);
                ImageIO.write(images[2], "png", files[2]);
                System.out.println("Keyframe " + c + " written.");
            }
            catch(IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    public void movingCheckerTransit() {
        int red2 = 0, green2 = 0, blue2 = 0, head2 = 0;
        int red1 = 0, green1 = 0, blue1 = 0, head1 = 0;
        int color, counter = 0;
        String rName1, rName2, wName3;
        int pXIndex, pYIndex;
        double pXFrac, pYFrac;
        double start;
        images[2] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        double cWidth = ((width + 0.0) / 2) + 0.5;
        double cHeight = ((height + 0.0) / 2) + 0.5;
        double[][][] reference_3D = new double[width][height][24];
        double[][][] cosX_Y_3D = new double[width][height][24];
        for(int c1 = 0; c1 < width; c1++) {
            for(int c2 = 0; c2 < height; c2++) {
                for(int c3 = 0; c3 < 24; c3++) {
                    cosX_Y_3D[c1][c2][c3] = Math.cos(((c3 * Math.PI) / 3.0) + ((c1 * Math.PI * 2) / 50.0)) * Math.cos(((c3 * Math.PI) / 3.0) + ((c2 * Math.PI * 2) / 50.0));
                    reference_3D[c1][c2][c3] = ((cosX_Y_3D[c1][c2][c3] * (keyframes - 1)) + keyframes - 1) / 2;      
                }
            }
        }
        for(int c = 1; c <= keyframes; c++) {
            try {
                rName1 = makeName(c, paths[0], ".jpg");
                rName2 = makeName(c, paths[1], ".jpg");
                wName3 = makeName(c, paths[2], ".png");
                files[0] = new File(rName1);
                images[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                images[0] = ImageIO.read(files[0]);
                files[1] = new File(rName2);
                images[1] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                images[1] = ImageIO.read(files[1]);
                start = 2 * width;
                for(int x = 0; x < width; x++) {
                    for(int y = 0; y < height; y++) {
                        if(c > reference_3D[x][y][c]) 
                            color = images[1].getRGB(x, y);
                        else
                            color = images[0].getRGB(x, y);
                        images[2].setRGB(x, y, color);
                    }
                }
                files[2] = new File(wName3);
                ImageIO.write(images[2], "png", files[2]);
                System.out.println("Keyframe " + c + " written.");
            }
            catch(IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    private void transit() {
        String pName1, pName2, pName3;
        for(int c = 1; c <= keyframes; c++) {
            pName1 = makeName(c, paths[0], ".jpg");
            pName2 = makeName(c, paths[1], ".jpg");
            pName3 = makeName(c, paths[2], ".png");
            try { //read in files
                images[2] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                files[0] = new File(pName1);
                images[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                images[0] = ImageIO.read(files[0]);
                files[1] = new File(pName2);
                images[1] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                images[1] = ImageIO.read(files[1]);
                double start = ((c) * stepSize) - (feather / 2.0);
                for(int x = 0; x < width; x++) {
                    for(int y = 0; y < height; y++) {
                        colors[0] = images[0].getRGB(x,y);
                        colors[1] = images[1].getRGB(x,y);
                        if(reference[x][y] < start) {
                            red = makeRed(colors[1]) & redMask;
                            green = makeGreen(colors[1]) & greenMask;
                            blue = makeBlue(colors[1]) & blueMask;
                            head = (makeHeader(colors[1]));
                            colors[2] = red + green + blue + hMask;
                            images[2].setRGB(x, y, colors[2]);
                        }
                        else if(reference[x][y] < start + feather) {
                            weight = (reference[x][y] - start) / feather;
                            red = ((int) (Math.round(((1 - weight) * makeRed(colors[1])) + (weight * makeRed(colors[0]))))) & redMask;
                            green = ((int) (Math.round(((1 - weight) * makeGreen(colors[1])) + (weight * makeGreen(colors[0]))))) & greenMask;
                            blue = ((int) (Math.round(((1 - weight) * makeBlue(colors[1])) + (weight * makeBlue(colors[0]))))) & blueMask;
                            head = (makeHeader(colors[0]));
                            colors[2] = red + green + blue + hMask;
                            images[2].setRGB(x, y, colors[2]);
                        }
                        else {
                            red = makeRed(colors[0]) & redMask;
                            green = makeGreen(colors[0]) & greenMask;
                            blue = makeBlue(colors[0]) & blueMask;
                            head = (makeHeader(colors[0]));
                            colors[2] = red + green + blue + hMask;
                            images[2].setRGB(x, y, colors[2]);
                        }
                    }
                }
                System.out.println("Keyframe " + c + " written.");
                files[2] = new File(pName3);
                ImageIO.write(images[2], "png", files[2]);
            }
            catch(IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    private static String makeName(int c, String pathName, String end) {
        if(c / 10 == 0 && c % 10 != 0)
            return (pathName + "00" + c + end);
        else if(c / 100 == 0 && c % 100 != 0)
            return (pathName + "0" + c + end);
        else
            return (pathName + c + end);
    }
    private static int makeRed(int colorInt) {
        return colorInt & redMask;
    }
    private static int makeBlue(int colorInt) {
        return colorInt & blueMask;
    }
    private static int makeGreen(int colorInt) {
        return colorInt & greenMask;
    }
    private static int makeHeader(int colorInt) {
        return colorInt & hMask;
    }
}