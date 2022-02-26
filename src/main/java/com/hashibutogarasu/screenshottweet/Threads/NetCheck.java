package com.hashibutogarasu.screenshottweet.Threads;

import net.minecraft.text.TranslatableText;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import static com.hashibutogarasu.screenshottweet.Ids.Id.MOD_ID;
import static com.hashibutogarasu.screenshottweet.Threads.TwitterThread.tweetstatuslabel;

public class NetCheck {
    public static void Checkinternetconnection(){

        int defaultcolor = tweetstatuslabel.getColor();

        try {
            URL url = new URL("http://twitter.com");
            URLConnection con = url.openConnection();
            con.getInputStream();
        } catch (IOException e) {
            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.connectionerror"));
            tweetstatuslabel.setColor(Color.RED.getRGB());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignore) { }

            tweetstatuslabel.setColor(defaultcolor);
            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel"));
            return;
        }
    }
}
