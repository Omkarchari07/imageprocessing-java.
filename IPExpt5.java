import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

class IPExpt5 extends JPanel {
    BufferedImage image;
    int[] histogram = new int[256];
    String title;

    IPExpt5(BufferedImage img, String title) {
        this.image = img;
        this.title = title;
        computeHistogram();
        setPreferredSize(new Dimension(500, 260));
    }

    void computeHistogram() {
        for (int i = 0; i < 256; i++) histogram[i] = 0;

        int w = image.getWidth();
        int h = image.getHeight();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = image.getRGB(x, y);
                int gray = rgb & 0xFF;  // grayscale pixel value
                histogram[gray]++;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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
    }

    public static void main(String[] args) {
        String[] files = {"high_contrast.png", "low_contrast.png"};
        String[] titles = {"High Contrast", "Low Contrast"};

        JFrame frame = new JFrame("High and Low Contrast Image Histograms");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 2, 10, 10));

        for (int i = 0; i < files.length; i++) {
            try {
                BufferedImage img = ImageIO.read(new File(files[i]));
                if (img == null) {
                    System.err.println("Cannot read " + files[i]);
                    continue;
                }

                // Convert to grayscale
                BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                Graphics g = gray.getGraphics();
                g.drawImage(img, 0, 0, null);
                g.dispose();

                frame.add(new IPExpt5(gray, titles[i]));

            } catch (IOException e) {
                System.err.println("Error loading " + files[i]);
                e.printStackTrace();
            }
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
