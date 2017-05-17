package com.cd.bot.client.tesseract;

import com.cd.bot.client.tesseract.model.TesseractRectangle;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.IOException;

/**
 * Created by Cory on 5/11/2017.
 */
public class ImagePreProcessor {

    @Autowired
    private Integer scalingFactor;

    public BufferedImage getGrayscaledImage(BufferedImage coloredImage) {
        ImageFilter filter = new ImageFilter(){
            public final int filterRGB(int x, int y, int rgb)
            {
                //TODO - optimization? Bit shifts, not this shits
                Color currentColor = new Color(rgb);
                if(currentColor.getRed() < 2 && currentColor.getGreen() < 2 && currentColor.getBlue() < 2) {
                    return new Color(rgb).darker().getRGB();
                }

                return Color.WHITE.getRGB();
            }
        };

        ImageProducer producer = new FilteredImageSource(coloredImage.getSource(), filter);
        Image image = Toolkit.getDefaultToolkit().createImage(producer);
        return toBufferedImage(image);
    }

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public BufferedImage preprocess(BufferedImage bi, TesseractRectangle targetArea) throws IOException {
        BufferedImage grayScaled = getGrayscaledImage(targetArea != null ? bi.getSubimage(targetArea.getLeft(),
                                                                                          targetArea.getTop(),
                                                                                          targetArea.getWidth(),
                                                                                          targetArea.getHeight())
                                                                                        : bi);

        return Thumbnails.of(grayScaled)
                         .scale(new Double(scalingFactor))
                         .outputQuality(1)
                         .asBufferedImage();
    }
}
