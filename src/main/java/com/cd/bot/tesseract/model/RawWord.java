package com.cd.bot.tesseract.model;

/**
 * Created by Cory on 5/14/2017.
 */
public class RawWord {
    private final String word;
    private final int left;
    private final int top;
    private final int right;
    private final int bottom;
    private final float confidence;

    public RawWord(String word, int left, int top, int right, int bottom, float confidence) {
        this.word = word;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.confidence = confidence;
    }

    public RawWord(final RawWord other) {
        this.word = other.getWord();
        this.left = other.getLeft();
        this.top = other.getTop();
        this.right = other.getRight();
        this.bottom = other.getBottom();
        this.confidence = other.getConfidence();
    }

    public float getConfidence() {
        return confidence;
    }

    public int getBottom() {
        return bottom;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public String getWord() {
        return word;
    }
}