package com.cd.bot.tesseract.model;

import java.util.List;

/**
 * Created by Cory on 5/11/2017.
 */
public class RawLines extends ScreenMeta {
    private List<String> rawLines;

    public List<String> getRawLines() {
        return rawLines;
    }

    public void setRawLines(List<String> rawLines) {
        this.rawLines = rawLines;
    }
}
