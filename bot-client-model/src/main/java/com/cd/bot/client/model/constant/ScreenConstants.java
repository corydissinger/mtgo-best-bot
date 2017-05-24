package com.cd.bot.client.model.constant;


import com.cd.bot.client.model.tesseract.TesseractRectangle;

/**
 * Created by Cory on 5/12/2017.
 */
public class ScreenConstants {
    //TODO - If you get to the point where this is on 1024x768, shaboing convert with maths

    public static final TesseractRectangle LOGIN_BOUNDS = new TesseractRectangle(1105, 459, 200, 170);
    public static final TesseractRectangle LOGIN_BUTTON_CENTER = new TesseractRectangle(1200, 515, 1, 1);

    public static final TesseractRectangle ACCEPT_TOS_BOUNDS = new TesseractRectangle(630, 290, 670, 490);
    public static final TesseractRectangle ACCEPT_TOS_CENTER = new TesseractRectangle(940, 540, 1, 1);
    public static final TesseractRectangle ACCEPT_TOS_SCROLL_TOP = new TesseractRectangle(1260, 360, 1, 1);

    public static final TesseractRectangle COLLECTION_BUTTON_CENTER = new TesseractRectangle(192, 56, 1, 1);
    public static final TesseractRectangle PARTNER_COLLECTION_LIST_BOUNDS = new TesseractRectangle(214, 76, 310, 555);
    public static final TesseractRectangle WELCOME_BOUNDS = new TesseractRectangle(34, 3, 293, 22);
}
