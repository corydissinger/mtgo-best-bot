package com.cd.bot.client.tesseract;

import java.util.List;

/**
 * Created by Cory on 5/11/2017.
 */
public class RawLines {
    private final List<List<RawWord>> rawLines;

    public RawLines(List<List<RawWord>> rawLines) {
        this.rawLines = rawLines;
    }

    public List<List<RawWord>> getRawLines() {
        return rawLines;
    }
}
