package com.cd.bot.tesseract;

import com.sun.jna.Pointer;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.TessAPI1;
import net.sourceforge.tess4j.util.ImageIOHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cory on 5/11/2017.
 */
public class TesseractWrapper {

    @Autowired
    private TessAPI1.TessBaseAPI tesseract;

    @Autowired
    private ImagePreProcessor imagePreProcessor;

    public List<String> getRawText(BufferedImage bi) {
        Long startTime = System.currentTimeMillis();
        BufferedImage newImage = null;

        try {
            newImage = imagePreProcessor.preprocess(bi);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File output = new File("C:\\testing\\mtgo-bot\\temp.png");

        try {
            ImageIO.write(newImage, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteBuffer buffer = ImageIOHelper.convertImageData(newImage);
        int bpp = newImage.getColorModel().getPixelSize();
        int bytespp = bpp / 8;
        int bytespl = (int) Math.ceil(newImage.getWidth() * bpp / 8.0);

        ITessAPI.ETEXT_DESC monitor = new ITessAPI.ETEXT_DESC();

        TessAPI1.TessBaseAPISetImage(tesseract, buffer, newImage.getWidth(), newImage.getHeight(), bytespp, bytespl);
        TessAPI1.TessBaseAPIRecognize(tesseract, monitor);

        ITessAPI.TessResultIterator ri = TessAPI1.TessBaseAPIGetIterator(tesseract);

        int level = ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE;

        List<String> rawLines = new ArrayList<>();
        List<Float> confidences = new ArrayList<>();

        do {
            Pointer symbol = TessAPI1.TessResultIteratorGetUTF8Text(ri, level);
            float conf = TessAPI1.TessResultIteratorConfidence(ri, level);

            confidences.add(conf);

            rawLines.add(symbol.getString(0));
        } while (TessAPI1.TessResultIteratorNext(ri, level) == ITessAPI.TRUE);
        Long endTime = System.currentTimeMillis();

        System.out.println("Avg conf: " + confidences.stream().reduce(0f, (sum, next) -> sum + next) / confidences.size());
        System.out.println("Completed in " + (endTime - startTime) + " milliseconds");

        return rawLines;
    }
}
