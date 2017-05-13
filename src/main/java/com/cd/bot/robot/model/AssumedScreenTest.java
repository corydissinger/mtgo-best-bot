package com.cd.bot.robot.model;

import com.cd.bot.robot.mtgo.ScreenConstants;
import com.cd.bot.tesseract.model.TesseractRectangle;

/**
 * Created by Cory on 5/13/2017.
 */
public enum AssumedScreenTest {
    NOT_NEEDED(null),
    LOGIN(ScreenConstants.LOGIN_BOUNDS),
    HOME_PAGE(ScreenConstants.WELCOME_BOUNDS);

    private final TesseractRectangle screenTestBounds;

    AssumedScreenTest(TesseractRectangle screenTestBounds) {
        this.screenTestBounds = screenTestBounds;
    }

    public TesseractRectangle getScreenTestBounds() {
        return screenTestBounds;
    }
}
