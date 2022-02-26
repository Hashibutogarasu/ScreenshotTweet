package com.hashibutogarasu.screenshottweet.Threads;

import net.minecraft.text.TranslatableText;

import java.awt.*;

import static com.hashibutogarasu.screenshottweet.Ids.Id.MOD_ID;
import static com.hashibutogarasu.screenshottweet.Threads.TwitterThread.defaultcolor;
import static com.hashibutogarasu.screenshottweet.Threads.TwitterThread.tweetstatuslabel;
import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.*;

public class TextFields {
    public static void Check(boolean tweet){
        if(tweettext.getText().length() == 0 && tweetimagedata.size() == 0){
            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.nontexterror"));
            tweetstatuslabel.setColor(Color.RED.getRGB());

            try {
                Thread.sleep(3500);
            } catch (InterruptedException ignore) { }

            tweetstatuslabel.setColor(defaultcolor);
            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel"));
        }
    }
}
