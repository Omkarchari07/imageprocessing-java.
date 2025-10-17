
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.IIOImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class IPExpr7Filter {
   private static final float[][] SIMPLE_AVERAGE_KERNEL = new float[][]{{0.11111111F, 0.11111111F, 0.11111111F}, {0.11111111F, 0.11111111F, 0.11111111F}, {0.11111111F, 0.11111111F, 0.11111111F}};
   private static final float[][] WEIGHTED_AVERAGE_KERNEL = new float[][]{{0.0625F, 0.125F, 0.0625F}, {0.125F, 0.25F, 0.125F}, {0.0625F, 0.125F, 0.0625F}};

   public IPExpr7Filter() {
   }

   public static void main(String[] var0) {
      String[] var1 = var0 != null && var0.length > 0 ? var0 : new String[]{"expt7.jpg"};
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];

         try {
            File var6 = new File(var5);
            if (!var6.exists()) {
               System.err.println("File not found: " + var5);
            } else {
               BufferedImage var7 = ImageIO.read(var6);
               if (var7 == null) {
                  System.err.println("Cannot read image: " + var5);
               } else {
                  BufferedImage var8 = toGrayscale(var7);
                  BufferedImage var9 = applyFilter(var8, SIMPLE_AVERAGE_KERNEL);
                  String var10 = buildOutputName(var5, "_simple_avg", "jpg");
                  writeJpeg(var9, new File(var10), 0.95f);
                  System.out.println("Saved simple averaging filtered image: " + var10);
                  BufferedImage var11 = applyFilter(var8, WEIGHTED_AVERAGE_KERNEL);
                  String var12 = buildOutputName(var5, "_weighted_avg", "jpg");
                  writeJpeg(var11, new File(var12), 0.95f);
                  System.out.println("Saved weighted averaging filtered image: " + var12);
               }
            }
         } catch (IOException var13) {
            System.err.println("Error processing " + var5);
            var13.printStackTrace();
         }
      }

   }

   private static BufferedImage toGrayscale(BufferedImage var0) {
   BufferedImage var1 = new BufferedImage(var0.getWidth(), var0.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
      Graphics2D var2 = var1.createGraphics();

      try {
         var2.drawImage(var0, 0, 0, (ImageObserver)null);
      } finally {
         var2.dispose();
      }

      return var1;
   }

   private static BufferedImage applyFilter(BufferedImage var0, float[][] var1) {
      int var2 = var0.getWidth();
      int var3 = var0.getHeight();
   BufferedImage var4 = new BufferedImage(var2, var3, BufferedImage.TYPE_BYTE_GRAY);
      int var5 = var1.length;
      int var6 = var5 / 2;

      for(int var7 = 0; var7 < var3; ++var7) {
         for(int var8 = 0; var8 < var2; ++var8) {
            float var9 = 0.0F;

            int var10;
            int var11;
            for(var10 = -var6; var10 <= var6; ++var10) {
               for(var11 = -var6; var11 <= var6; ++var11) {
                  int var12 = clamp(var8 + var11, 0, var2 - 1);
                  int var13 = clamp(var7 + var10, 0, var3 - 1);
                  int var14 = var0.getRGB(var12, var13);
                  int var15 = var14 & 255;
                  float var16 = var1[var10 + var6][var11 + var6];
                  var9 += (float)var15 * var16;
               }
            }

            var10 = Math.min(255, Math.max(0, Math.round(var9)));
            var11 = var10 << 16 | var10 << 8 | var10;
            var4.setRGB(var8, var7, -16777216 | var11);
         }
      }

      return var4;
   }

   private static int clamp(int var0, int var1, int var2) {
      return Math.max(var1, Math.min(var2, var0));
   }

   private static String buildOutputName(String var0, String var1, String var2) {
      int var3 = var0.lastIndexOf(46);
      String var4 = var3 > 0 ? var0.substring(0, var3) : var0;
      return var4 + var1 + "." + var2;
   }

   // Write a BufferedImage as high-quality JPEG, converting to RGB if needed
   private static void writeJpeg(BufferedImage src, File outFile, float quality) throws IOException {
      BufferedImage rgb;
      if (src.getType() != BufferedImage.TYPE_INT_RGB) {
         rgb = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
         Graphics2D g2 = rgb.createGraphics();
         try {
            g2.drawImage(src, 0, 0, (ImageObserver)null);
         } finally {
            g2.dispose();
         }
      } else {
         rgb = src;
      }

      java.util.Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
      if (!writers.hasNext()) {
         throw new IOException("No JPEG ImageWriter available");
      }
      ImageWriter writer = writers.next();
      ImageWriteParam param = writer.getDefaultWriteParam();
      if (param.canWriteCompressed()) {
         param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
         param.setCompressionQuality(Math.max(0f, Math.min(1f, quality)));
      }
      try (ImageOutputStream ios = ImageIO.createImageOutputStream(outFile)) {
         writer.setOutput(ios);
         writer.write(null, new IIOImage(rgb, null, null), param);
      } finally {
         writer.dispose();
      }
   }
}
