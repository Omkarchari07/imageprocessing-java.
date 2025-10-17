import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import javax.imageio.ImageIO;

public class GradientImages {
    public static void main(String[] args) {
        int width = 256;
        int height = 256;

        BufferedImage darkToLight = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage lightToDark = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Dark to Light (top to bottom)
        for (int y = 0; y < height; y++) {
            int gray = y; // 0 at top, 255 at bottom
            if (gray > 255) gray = 255;
            Color color = new Color(gray, gray, gray);
            for (int x = 0; x < width; x++) {
                darkToLight.setRGB(x, y, color.getRGB());
            }
        }

        // Light to Dark (top to bottom)
        for (int y = 0; y < height; y++) {
            int gray = 255 - y; // 255 at top, 0 at bottom
            if (gray < 0) gray = 0;
            Color color = new Color(gray, gray, gray);
            for (int x = 0; x < width; x++) {
                lightToDark.setRGB(x, y, color.getRGB());
            }
        }

        try {
            ImageIO.write(darkToLight, "png", new File("gradient_dark_to_light.png"));
            ImageIO.write(lightToDark, "png", new File("gradient_light_to_dark.png"));
            System.out.println("Images created: gradient_dark_to_light.png, gradient_light_to_dark.png");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}