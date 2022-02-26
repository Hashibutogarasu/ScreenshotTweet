package com.hashibutogarasu.screenshottweet.Threads;

import net.minecraft.text.TranslatableText;

import static com.hashibutogarasu.screenshottweet.Ids.Id.MOD_ID;
import static com.hashibutogarasu.screenshottweet.Ids.Identifiers.none;
import static com.hashibutogarasu.screenshottweet.Threads.TwitterThread.defaultcolor;
import static com.hashibutogarasu.screenshottweet.Threads.TwitterThread.tweetstatuslabel;
import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.*;
import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.buttonManager;

public class Default {
    public static void Set() {
        if (tweetimagelist != null && tweetimagedata != null) {
            tweetimagelist.clear();
            tweetimagedata.clear();
        }

        imagescount.setText(new TranslatableText(MOD_ID + ".gui.imagescountdefault"));
        buttonManager.setEnabled(true);
    }

    public static void reset(){
        tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel"));
        tweetstatuslabel.setColor(defaultcolor);
        root.remove(loadingstatusimage);
        root.remove(tweetstatuslabel);
        statusimage.setImage(none);
    }
}
