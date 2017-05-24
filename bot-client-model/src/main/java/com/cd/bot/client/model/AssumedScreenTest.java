package com.cd.bot.client.model;


import com.cd.bot.client.model.constant.ScreenConstants;
import com.cd.bot.client.model.tesseract.TesseractRectangle;

/**
 * Created by Cory on 5/13/2017.
 */
public enum AssumedScreenTest {
    NOT_NEEDED(null),
    LOGIN(ScreenConstants.LOGIN_BOUNDS),
    HOME_PAGE(ScreenConstants.WELCOME_BOUNDS),
    COLLECTION_BOUNDS(ScreenConstants.PARTNER_COLLECTION_LIST_BOUNDS),
    TRADE(ScreenConstants.PARTNER_COLLECTION_LIST_BOUNDS),
    EULA(ScreenConstants.ACCEPT_TOS_BOUNDS);

    private final TesseractRectangle screenTestBounds;

    AssumedScreenTest(TesseractRectangle screenTestBounds) {
        this.screenTestBounds = screenTestBounds;
    }

    public TesseractRectangle getScreenTestBounds() {
        return screenTestBounds;
    }
}
