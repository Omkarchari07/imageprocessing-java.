import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class calculates the grayscale histogram and manually draws the result 
 * as a bar chart onto a new image file named "histogram_visual.png".
 * * NOTE: Ensure the input image ("input.jpg") is in the same folder.
 */
public class VisualHistogramGenerator {

    private static final int NUM_LEVELS = 256;
    
    // --- Configuration for the output chart image ---
    private static final int CHART_WIDTH = 512; // 2 pixels per bin (256 * 2)
    private static final int CHART_HEIGHT = 200;
    private static final int PADDING = 20; // Space for labels and margins
    private static final int TOTAL_WIDTH = CHART_WIDTH + PADDING * 2;
    private static final int TOTAL_HEIGHT = CHART_HEIGHT + PADDING * 2;


    public static void main(String[] args) {
        String imagePath = "grayscale_image.jpg"; // Change to your image file path
        File imageFile = new File(imagePath);
        BufferedImage image = null;

        try {
            // --- 1. Load the Image ---
            image = ImageIO.read(imageFile);
            
            if (image == null) {
                System.out.println("ERROR: Could not read the image file at: " + imagePath);
                System.out.println("Please check the file name and path.");
                return;
            }

            // --- 2. Calculate the Histogram Data (Frequency Counts) ---
            long[] histGray = calculateGrayscaleHistogram(image);
            
            // --- 3. Find the Maximum Count for Scaling ---
            long maxCount = 0;
            for (long count : histGray) {
                if (count > maxCount) {
                    maxCount = count;
                }
            }
            if (maxCount == 0) {
                 System.out.println("ERROR: Image has no pixels or is corrupted.");
                 return;
            }

            // --- 4. Draw the Visual Histogram ---
            BufferedImage histImage = drawHistogram(histGray, maxCount);

            // --- 5. Save the Image File ---
            File outputfile = new File("histogram_visual.png");
            ImageIO.write(histImage, "png", outputfile);
            
            System.out.println("SUCCESS: Histogram visual saved to: " + outputfile.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("An I/O error occurred: " + e.getMessage());
        }
    }
    
    /**
     * Calculates the grayscale histogram counts for the image.
     */
    private static long[] calculateGrayscaleHistogram(BufferedImage image) {
        long[] histGray = new long[NUM_LEVELS]; 
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);

                // Extract RGB values
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Calculate Grayscale Intensity (Luminance formula)
                int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                gray = Math.min(255, Math.max(0, gray)); 

                histGray[gray]++;
            }
        }
        return histGray;
    }

    /**
     * Draws the histogram data onto a new BufferedImage.
     */
    private static BufferedImage drawHistogram(long[] hist, long maxCount) {
        // Create the canvas for the chart
        BufferedImage output = new BufferedImage(TOTAL_WIDTH, TOTAL_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = output.createGraphics();

        // --- Setup Background and Axes ---
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, TOTAL_WIDTH, TOTAL_HEIGHT);
        
        // Draw the main chart area background
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawRect(PADDING, PADDING, CHART_WIDTH, CHART_HEIGHT);
        
        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.drawString("Grayscale Histogram (0=Black, 255=White)", PADDING, PADDING - 5);


        // --- Draw Histogram Bars ---
        int chartYStart = PADDING + CHART_HEIGHT; // Y coordinate of the bottom line

        for (int i = 0; i < NUM_LEVELS; i++) {
            // Calculate bar height relative to maxCount and CHART_HEIGHT
            double normalizedHeight = (double) hist[i] / maxCount;
            int barHeight = (int) (normalizedHeight * CHART_HEIGHT);
            
            // Set position (X: PADDING + i * 2, Y: bottom line - bar height)
            int x = PADDING + (i * 2);
            int y = chartYStart - barHeight;
            
            // Draw the bar
            g2d.setColor(Color.BLUE.darker());
            g2d.fillRect(x, y, 2, barHeight);
        }

        // --- Draw X-axis labels (for 0, 128, 255) ---
        g2d.setColor(Color.BLACK);
        g2d.drawString("0", PADDING, chartYStart + 15);
        g2d.drawString("128", PADDING + CHART_WIDTH / 2 - 10, chartYStart + 15);
        g2d.drawString("255", PADDING + CHART_WIDTH - 20, chartYStart + 15);
        
        // Draw Y-axis label (Max Count)
        String maxCountLabel = String.format("Max: %,d", maxCount);
        g2d.drawString(maxCountLabel, 5, PADDING + 10);


        g2d.dispose();
        return output;
    }
}