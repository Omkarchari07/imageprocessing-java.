import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import javax.imageio.ImageIO;

public class ColorToGray {
    public static void main(String[] args) {
        try {
            // Load input image (color)
            File input = new File("color_image.jpg"); // replace with your file name
            BufferedImage image = ImageIO.read(input);

            // Create output image (grayscale)
            BufferedImage grayImage = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY
            );

            // Loop through each pixel
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    // Get RGB value
                    Color color = new Color(image.getRGB(x, y));

                    // Convert to grayscale using luminosity method
                    int gray = (int)(0.299 * color.getRed() +
                                     0.587 * color.getGreen() +
                                     0.114 * color.getBlue());

                    // Set pixel in grayscale image
                    Color newColor = new Color(gray, gray, gray);
                    grayImage.setRGB(x, y, newColor.getRGB());
                }
            }

            // Save output image
            File output = new File("grayscale_image.jpg");
            ImageIO.write(grayImage, "jpg", output);

            System.out.println("Conversion complete. Saved as grayscale_image.jpg");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
