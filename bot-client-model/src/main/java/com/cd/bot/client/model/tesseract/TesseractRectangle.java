package com.cd.bot.client.model.tesseract;

/**
 * Created by Cory on 5/13/2017.
 */
public class TesseractRectangle {
    public TesseractRectangle() {}

    public TesseractRectangle(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    private int left;
    private int top;
    private int width;
    private int height;

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
