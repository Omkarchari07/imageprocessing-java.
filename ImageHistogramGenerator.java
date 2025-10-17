import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Arrays;


public class ImageHistogramGenerator {

    // Define the size of the histogram array (256 levels for 0 to 255)
    private static final int NUM_LEVELS = 256;

    public static void main(String[] args) {
        // --- 1. SET IMAGE FILE PATH ---
        // Change "input.jpg" to the actual name or full path of your image file.
        String imagePath = "image.png"; 

        File imageFile = new File(imagePath);
        BufferedImage image = null;

        try {
            // Read the image file
            image = ImageIO.read(imageFile);
            
            if (image == null) {
                System.out.println("ERROR: Could not read the image file at: " + imagePath);
                System.out.println("Please check if the file exists and is a valid image format (e.g., jpg, png).");
                return;
            }

            // --- 2. INITIALIZE HISTOGRAM ARRAY ---
            // Array to store the count (frequency) for each grayscale intensity level (0 to 255)
            long[] histGray = new long[NUM_LEVELS]; 

            // Get image dimensions
            int width = image.getWidth();
            int height = image.getHeight();
            
            System.out.println("Processing image: " + imagePath);
            System.out.println("Dimensions: " + width + "x" + height + " pixels.");

            // --- 3. ITERATE THROUGH ALL PIXELS ---
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Get the 32-bit integer RGB value for the current pixel
                    int pixel = image.getRGB(x, y);

                    // Extract the intensity (color) value for each channel (0-255)
                    int red = (pixel >> 16) & 0xFF;
                    int green = (pixel >> 8) & 0xFF;
                    int blue = pixel & 0xFF;

                    // --- 4. CALCULATE GRAYSCALE INTENSITY (LUMINANCE) ---
                    // Grayscale intensity is often calculated as a weighted average (Luminance)
                    // to better match human perception of brightness.
                    // Luminance formula: L = 0.299*R + 0.587*G + 0.114*B
                    int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

                    // Ensure the calculated value is within the 0-255 range (though usually it is)
                    gray = Math.min(255, Math.max(0, gray)); 

                    // --- 5. INCREMENT HISTOGRAM COUNT ---
                    histGray[gray]++;
                }
            }

            // --- 6. PRINT THE RESULT ---
            System.out.println("\n--- GRAYSCALE HISTOGRAM DATA (Intensity Level: Frequency Count) ---");
            System.out.println("\nGRAYSCALE CHANNEL HISTOGRAM:");
            printHistogram(histGray);

        } catch (IOException e) {
            System.out.println("An error occurred while reading the image file: " + e.getMessage());
        }
    }

    /**
     * Helper function to neatly print the histogram array.
     * @param hist The histogram array (frequency counts).
     */
    private static void printHistogram(long[] hist) {
        for (int i = 0; i < hist.length; i++) {
            if (hist[i] > 0) {
                // Print only the intensity levels that actually appear in the image
                // Format: [Intensity Level (0-255)]: [Count]
                System.out.printf("[%3d]: %d\n", i, hist[i]);
            }
        }
    }
}