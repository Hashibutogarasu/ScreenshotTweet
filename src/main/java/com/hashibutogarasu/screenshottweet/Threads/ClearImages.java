package com.hashibutogarasu.screenshottweet.Threads;

import com.hashibutogarasu.screenshottweet.ScreenshotTweetModClient;
import net.minecraft.text.TranslatableText;

import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.*;

public class ClearImages implements Runnable {
    public ClearImages(){
        try {
            if((long) tweetimagelist.size() != 0 && (long) tweetimagedata.size() != 0){
                tweetimagelist.clear();
                tweetimagedata.clear();
            }

            imagescount.setText(new TranslatableText("screenshottweet.gui.imagescountdefault"));
            buttonManager.setEnabled(true);

            ScreenshotTweetModClient.LOGGER.info("Images Cleared");

        }catch (Exception ignored) {

        }
    }

    @Override
    public void run() {

    }
}
