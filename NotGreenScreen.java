import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class NotGreenScreen { //works with any background, so long as it doesn't change
    private static int redMask = 0xFF0000, blueMask = 0xFF00, greenMask = 0xFF, hMask = 0xFF000000;
    private int width, height, keyframes;
    String[] paths;
    private File[] files;
    private BufferedImage[] images;
    private int threshold = 0;
    public NotGreenScreen(int w, int h, int k, String[] p) {
        width = w;
        height = h;
        keyframes = k;
        paths = new String[3]; 
        paths[0] = p[0];
        paths[1] = p[1];
        paths[2] = p[2];
        files = new File[3];
        images = new BufferedImage[3];
    }
    public static void main(String[] args) {
        String[] pathnames = new String[3];
        pathnames[0] = "C:\\Users\\Bella\\Documents\\Transition Hold\\DSC00021_001.jpg";
        pathnames[1] = "C:\\Users\\Bella\\Documents\\Transition Hold\\DSC00022_";
        pathnames[2] = "C:\\Users\\Bella\\Documents\\Transition Hold\\gs3_";
        int width = 5472, height = 3648, keyframes = 1;
        NotGreenScreen ngs = new NotGreenScreen(width, height, keyframes, pathnames);
        ngs.run();
    }
    public void run() {
        String pName1, pName2, pName3;
        for(int c = 1; c <= keyframes; c++) {
            pName1 = paths[0];//makeName(c, paths[0], ".jpg");
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
                subtract();
                files[2] = new File(pName3);
                ImageIO.write(images[2], "png", files[2]);
                System.out.println("Keyframe " + c + " written.");
            }
            catch(IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    public void subtract() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(same(x, y)) {
                    images[2].setRGB(x, y, 1000);
                }
                else {
                    images[2].setRGB(x, y, images[1].getRGB(x, y));
                }
            }
        }
    }
    private boolean same(int x, int y) {
        int back = images[0].getRGB(x, y);
        int front = images[1].getRGB(x, y);
        if(makeRed(back) - makeRed(front) > threshold) {
            return false;
        }
        if(makeGreen(back) - makeGreen(front) > threshold) {
            return false;
        }
        if(makeBlue(back) - makeBlue(front) > threshold) {
            return false;
        }
        return true;
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
