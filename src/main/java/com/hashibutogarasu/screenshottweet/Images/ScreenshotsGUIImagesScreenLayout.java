package com.hashibutogarasu.screenshottweet.Images;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;

public class ScreenshotsGUIImagesScreenLayout extends WPlainPanel {
    public static WLabel label;

    public ScreenshotsGUIImagesScreenLayout() {

        label = new WLabel("");
        this.add(label, 0, 2, 4*18, 10);

        this.setSize(7*18, 2*13);
    }
}
