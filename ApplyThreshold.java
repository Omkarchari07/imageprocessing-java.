import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ApplyThreshold {
    // Set your threshold value here
    private static final int THRESHOLD = 200; // Change this value and re-run

    public static void main(String[] args) {
        String inputImageName = "output_negative.jpg"; // Input image in the same folder
        File inputFile = new File(inputImageName);

        if (!inputFile.exists()) {
            System.out.println("Input image not found: " + inputImageName);
            return;
        }

        try {
            BufferedImage src = ImageIO.read(inputFile);
            BufferedImage result = threshold(src, THRESHOLD);
            String outputName = "output_negative_th" + THRESHOLD + ".jpg";
            File output = new File(outputName);
            ImageIO.write(result, "jpg", output);
            System.out.println("Saved: " + output.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Failed to process: " + inputImageName);
            e.printStackTrace();
        }
    }

    // Thresholding method
    public static BufferedImage threshold(BufferedImage src, int threshold) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = src.getRaster().getSample(x, y, 0);
                int value = (gray >= threshold) ? 255 : 0;
                result.getRaster().setSample(x, y, 0, value);
            }
        }
        return result;
    }
}