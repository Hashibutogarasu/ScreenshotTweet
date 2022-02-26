package com.hashibutogarasu.screenshottweet.Custom;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.minecraft.text.TranslatableText;

import static com.hashibutogarasu.screenshottweet.Ids.Id.MOD_ID;
import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.root;
import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.tweettext;

public class CustomTextField extends WTextField {
    public void refresh() throws InterruptedException {
        tweettext.setSize(2000,12);
        Thread.sleep(100);
        tweettext.setText(new TranslatableText(MOD_ID + ".gui.tweettextfield").getString());
        Thread.sleep(100);
        root.remove(tweettext);
        Thread.sleep(100);
        root.add(tweettext, 13, 2, 7, 12);
    }
}
