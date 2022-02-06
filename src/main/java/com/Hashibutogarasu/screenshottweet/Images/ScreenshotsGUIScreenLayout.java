package com.Hashibutogarasu.screenshottweet.Images;

import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.text.TranslatableText;

public class ScreenshotsGUIScreenLayout extends WPlainPanel {
    public static WLabel label;
    public static WButton AddButton;
    public static WButton OpenButton;

    public ScreenshotsGUIScreenLayout() {

        label = new WLabel("");
        this.add(label, 18+ 4, 2, 5*18, 15);

        AddButton = new WButton();
        AddButton.setLabel(new TranslatableText(""));
        this.add(AddButton, 18 + 4, 17, (5 * 18) - 3, 15);

        OpenButton = new WButton();
        OpenButton.setLabel(new TranslatableText(""));
        this.add(OpenButton, (5 * 18) + 20, 17, 5 * 18, 15);

        this.setSize(7*18, 2*13);
    }
}
