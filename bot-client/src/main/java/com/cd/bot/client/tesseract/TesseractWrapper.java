package com.cd.bot.client.tesseract;

import com.cd.bot.client.tesseract.model.RawLines;
import com.cd.bot.client.tesseract.model.RawWord;
import com.cd.bot.client.tesseract.model.TesseractRectangle;
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
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cory on 5/11/2017.
 */
public class TesseractWrapper {

    @Autowired
    private TessAPI1.TessBaseAPI tesseract;

    @Autowired
    private ImagePreProcessor imagePreProcessor;

    @Autowired
    private Integer scalingFactor;

    public RawLines getRawText(BufferedImage bi, TesseractRectangle targetArea) {
        Long startTime = System.currentTimeMillis();
        BufferedImage newImage = null;

        try {
            newImage = imagePreProcessor.preprocess(bi, targetArea);
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
        ITessAPI.TessPageIterator pi = TessAPI1.TessResultIteratorGetPageIterator(ri);

        int level = ITessAPI.TessPageIteratorLevel.RIL_WORD;

        List<RawWord> currentLineWords = new ArrayList<>();
        List<List<RawWord>> allLines = new ArrayList<>();
        Integer lastTop = null;

        do {
            final Pointer symbol = TessAPI1.TessResultIteratorGetUTF8Text(ri, level);

            if(symbol == null) {
                continue;
            }

            final String word = symbol.getString(0);

            IntBuffer leftB = IntBuffer.allocate(1);
            IntBuffer topB = IntBuffer.allocate(1);
            IntBuffer rightB = IntBuffer.allocate(1);
            IntBuffer bottomB = IntBuffer.allocate(1);
            TessAPI1.TessPageIteratorBoundingBox(pi, level, leftB, topB, rightB, bottomB);

            final float conf = TessAPI1.TessResultIteratorConfidence(ri, level);

            RawWord newWord = new RawWord(word, leftB.get(), topB.get(), rightB.get(), bottomB.get(), conf);

            if(lastTop != null && !(lastTop - 2 < newWord.getTop() && lastTop + 2 > newWord.getTop())) {
                allLines.add(currentLineWords.stream().map(oldWord -> new RawWord(oldWord)).collect(Collectors.toList()));
                currentLineWords = new ArrayList<>();
            }

            currentLineWords.add(newWord);

            lastTop = newWord.getTop();
        } while (TessAPI1.TessResultIteratorNext(ri, level) == ITessAPI.TRUE);

        allLines.add(currentLineWords);

        Long endTime = System.currentTimeMillis();

        System.out.println("Completed in " + (endTime - startTime) + " milliseconds");

        return new RawLines(allLines);
    }

    public RawLines getRawText(BufferedImage bi) {
        return getRawText(bi, null);
    }
}
