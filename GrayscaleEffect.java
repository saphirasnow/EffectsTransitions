import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.*;
import javax.imageio.*;

public class GrayscaleEffect {
    public static int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF, hMask = 0xFF000000;
    public static void main(String[] args) {
        int width = 1280;
        int height = 720;
        int transits = 3;
        BufferedImage imageR = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageW = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        File fileR = null, fileW = null;
        String rName = "C:\\Users\\Bella\\Documents\\Transition Hold\\I2_";
        String wName = "C:\\Users\\Bella\\Documents\\Transition Hold\\I3_";
        String readName, writeName;
        Color colorR, colorW;
        int red, green, blue;
        for(int c = 1; c <= transits; c++) {
            if(c / 10 == 0 && c % 10 != 0) {
                readName = rName + "00" + c + ".jpg";
                writeName = wName + "00" + c + ".png";
            }
            else if(c / 100 == 0 && c % 100 != 0) {
                readName = rName + "0" + c + ".jpg";
                writeName = wName + "0" + c + ".png";
            }
            else {
                readName = rName + c + ".jpg";
                writeName = wName + c + ".png";
            }
            try {
                fileR = new File(readName);
                imageR = ImageIO.read(fileR);
                System.out.println("Read");
                for(int x = 0; x < width; x++) {
                    for(int y = 0; y < height; y++) {
                        colorR = new Color(imageR.getRGB(x, y));
                        int avg = (colorR.getRed() + colorR.getGreen() + colorR.getBlue()) / 3;
                        colorW = new Color(avg, avg, avg);
                        imageW.setRGB(x, y, colorW.getRGB());
                    }
                }
                fileW = new File(writeName);
                ImageIO.write(imageW, "png", fileW);
                System.out.println("Written " + c);
            }
            catch(IOException e) {
                System.out.println("Error: " + e.getMessage() + rName);
            }
        }
    }
}
