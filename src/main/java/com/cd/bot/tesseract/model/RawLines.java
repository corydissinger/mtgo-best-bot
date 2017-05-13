package com.cd.bot.tesseract.model;

import java.util.List;

/**
 * Created by Cory on 5/11/2017.
 */
public class RawLines extends ScreenMeta {
    private List<String> rawLines;
    private List<Float> confidences;

    public List<String> getRawLines() {
        return rawLines;
    }

    public void setRawLines(List<String> rawLines) {
        this.rawLines = rawLines;
    }

    public List<Float> getConfidences() {
        return confidences;
    }

    public void setConfidences(List<Float> confidences) {
        this.confidences = confidences;
    }
}
