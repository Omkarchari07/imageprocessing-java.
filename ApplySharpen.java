import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import javax.imageio.ImageIO;

public class ApplySharpen {
    public static void main(String[] args) {
        String inputImageName = "image.png"; // Input image in the same folder
        File inputFile = new File("image.png");

        if (!inputFile.exists()) {
            System.out.println("Input image not found: " + inputImageName);
            return;
        }

        try {
            // Read input image
            BufferedImage src = ImageIO.read(inputFile);

            // Apply sharpening
            BufferedImage result = sharpen(src);

            // Save output image
            String outputName = "image_sharpened.jpg";
            File output = new File(outputName);
            ImageIO.write(result, "jpg", output);

            System.out.println("Sharpened image saved: " + output.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Failed to process: " + inputImageName);
            e.printStackTrace();
        }
    }

    // Sharpening method using convolution
    public static BufferedImage sharpen(BufferedImage src) {
        // Define sharpening kernel
        float[] sharpenKernel = {
             0f, -1f,  0f,
            -1f,  5f, -1f,
             0f, -1f,  0f
        };

        Kernel kernel = new Kernel(3, 3, sharpenKernel);

        // Apply convolution
        ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return convolve.filter(src, null);
    }
}
