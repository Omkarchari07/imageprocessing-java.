import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IPExpt5Filters {

    // Simple averaging filter kernel (3x3)
    private static final float[][] SIMPLE_AVERAGE_KERNEL = {
        {1/9f, 1/9f, 1/9f},
        {1/9f, 1/9f, 1/9f},
        {1/9f, 1/9f, 1/9f}
    };

    // Weighted averaging filter kernel (Gaussian-like 3x3)
    private static final float[][] WEIGHTED_AVERAGE_KERNEL = {
        {1/16f, 2/16f, 1/16f},
        {2/16f, 4/16f, 2/16f},
        {1/16f, 2/16f, 1/16f}
    };

    public static void main(String[] args) {
        String[] files = {"grayscale_image.jpg"};

        for (String filename : files) {
            try {
                BufferedImage img = ImageIO.read(new File(filename));
                if (img == null) {
                    System.err.println("Cannot read " + filename);
                    continue;
                }

                // Convert to grayscale if needed
                BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                gray.getGraphics().drawImage(img, 0, 0, null);

                // Apply simple average filter
                BufferedImage simpleFiltered = applyFilter(gray, SIMPLE_AVERAGE_KERNEL);
                String simpleName = filename.replace(".png", "_simple_avg.png");
                ImageIO.write(simpleFiltered, "png", new File(simpleName));
                System.out.println("Saved simple averaging filtered image: " + simpleName);

                // Apply weighted average filter
                BufferedImage weightedFiltered = applyFilter(gray, WEIGHTED_AVERAGE_KERNEL);
                String weightedName = filename.replace(".png", "_weighted_avg.png");
                ImageIO.write(weightedFiltered, "png", new File(weightedName));
                System.out.println("Saved weighted averaging filtered image: " + weightedName);

            } catch (IOException e) {
                System.err.println("Error processing " + filename);
                e.printStackTrace();
            }
        }
    }

    // Apply convolution filter to grayscale image
    private static BufferedImage applyFilter(BufferedImage img, float[][] kernel) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        int kSize = kernel.length;
        int kHalf = kSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float sum = 0f;

                for (int ky = -kHalf; ky <= kHalf; ky++) {
                    for (int kx = -kHalf; kx <= kHalf; kx++) {
                        int px = clamp(x + kx, 0, width -1);
                        int py = clamp(y + ky, 0, height -1);

                        int rgb = img.getRGB(px, py);
                        int gray = rgb & 0xFF;

                        float weight = kernel[ky + kHalf][kx + kHalf];
                        sum += gray * weight;
                    }
                }

                int val = Math.min(255, Math.max(0, Math.round(sum)));
                int newRgb = (val << 16) | (val << 8) | val; // grayscale rgb

                output.setRGB(x, y, (0xFF << 24) | newRgb);
            }
        }
        return output;
    }

    // Helper to clamp value within bounds
    private static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
