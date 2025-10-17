import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageNegative {
    public static void main(String[] args) {
        try {
            // Load the input image
            File input = new File("grayscale_image.jpg"); // Change to your input image file
            BufferedImage image = ImageIO.read(input);

            // Get image dimensions
            int width = image.getWidth();
            int height = image.getHeight();

           for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
        int p = image.getRGB(x, y);

        int a = (p >> 24) & 0xff;
        int gray = p & 0xff; // Since R=G=B in grayscale

        // Invert the grayscale value
        gray = 255 - gray;

        // Compose the new pixel (same value for R, G, B)
        p = (a << 24) | (gray << 16) | (gray << 8) | gray;
        image.setRGB(x, y, p);
    }
}

            // Save the negative image
            File output = new File("output_negative.jpg");
            ImageIO.write(image, "jpg", output);

            System.out.println("Negative image created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}