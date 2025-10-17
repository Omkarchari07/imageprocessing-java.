import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
class IPExpt5 {
    BufferedImage image;
    int[] histogram = new int[256];
    String title;

    IPExpt5(BufferedImage img, String title) {
        this.image = img;
        this.title = title;
        computeHistogram();
    }

    void computeHistogram() {
        for (int i = 0; i < 256; i++) histogram[i] = 0;

        int w = image.getWidth();
        int h = image.getHeight();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = image.getRGB(x, y);
                int gray = rgb & 0xFF;
                histogram[gray]++;
            }
        }
    }

    BufferedImage createVisualization() {
        BufferedImage output = new BufferedImage(500, 260, BufferedImage.TYPE_INT_RGB);
        Graphics g = output.getGraphics();
        
        // White background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 500, 260);
        
        // Draw image (scaled)
        g.drawImage(image, 10, 10, 200, 200, null);

        // Draw histogram box
        int histX = 220, histWidth = 256, histHeight = 200;
        g.setColor(Color.BLACK);
        g.drawRect(histX, 10, histWidth, histHeight);

        // Find max histogram count
        int max = 0;
        for (int v : histogram) if (v > max) max = v;

        // Draw histogram bars
        g.setColor(Color.GRAY);
        for (int i = 0; i < 256; i++) {
            int scaled = (int)((double)histogram[i] / max * histHeight);
            g.drawLine(histX + i, 10 + histHeight, histX + i, 10 + histHeight - scaled);
        }

        // Draw title
        g.setColor(Color.BLACK);
        g.drawString(title, 10, 230);
        
        g.dispose();
        return output;
    }

    public static void main(String[] args) {
        String[] files = {"high_contrast.png", "low_contrast.png"};
        String[] titles = {"High Contrast", "Low Contrast"};
        // Create combined output image
        BufferedImage combined = new BufferedImage(1000, 260, BufferedImage.TYPE_INT_RGB);
        Graphics g = combined.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1000, 260);

        int xOffset = 0;
        for (int i = 0; i < files.length; i++) {
            try {
                BufferedImage img = ImageIO.read(new File(files[i]));
                if (img == null) {
                    System.err.println("Cannot read " + files[i]);
                    continue;
                }

                // Convert to grayscale
                BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                Graphics g2 = gray.getGraphics();
                g2.drawImage(img, 0, 0, null);
                g2.dispose();

                IPExpt5 panel = new IPExpt5(gray, titles[i]);
                BufferedImage viz = panel.createVisualization();
                g.drawImage(viz, xOffset, 0, null);
                xOffset += 500;

            } catch (IOException e) {
                System.err.println("Error loading " + files[i]);
                e.printStackTrace();
            }
        }
        g.dispose();

        // Save output
        try {
            ImageIO.write(combined, "png", new File("histogram_output.png"));
            System.out.println("Output saved to histogram_output.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
