package com.hashibutogarasu.screenshottweet.Threads;

import com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory;
import com.hashibutogarasu.screenshottweet.ScreenshotTweetModClient;
import net.minecraft.text.TranslatableText;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.util.ArrayList;

import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.*;

public class TwitterThread implements Runnable {
    public TwitterThread(boolean tweet){
        try {
            tweetimagelist = new ArrayList<>();

            for (String imagespath : tweetimagedata) {
                if (!imagespath.isBlank() || !imagespath.isEmpty() || imagespath != null) {
                    tweetimagelist.add(imagespath);
                }
            }

            ScreenshotTweetModClient.LOGGER.info("Tweet:" + tweettext.getText());

            for (String imagelist : tweetimagelist) {
                ScreenshotTweetModClient.LOGGER.info(imagelist);
            }

            try {
                ConfigurationBuilder cb = new ConfigurationBuilder();

                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.apikey)
                        .setOAuthConsumerSecret(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.apikeysecret)
                        .setOAuthAccessToken(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.accesstoken)
                        .setOAuthAccessTokenSecret(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.accesstokensecret);

                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();

                ScreenshotTweetModClient.LOGGER.info("Logged in as:" + twitter.getScreenName());

                ArrayList<UploadedMedia> medias = new ArrayList<>();

                if (tweetimagelist.stream().count() != 0) {
                    try {
                        for (String p : tweetimagelist) {
                            medias.add(twitter.uploadMedia(new File(p)));
                        }
                    } catch (TwitterException e) {
                        ScreenshotTweetModClient.LOGGER.info(e.toString());
                    }
                }

                if (tweettext.getText().length() != 0 || tweetimagelist.stream().count() >= 1) {

                    StatusUpdate update = new StatusUpdate(tweettext.getText());

                    long[] mediaIds = new long[(int) tweetimagelist.stream().count()];

                    UploadedMedia media;
                    if(tweet) {
                        if (tweetimagelist.stream().count() != 0) {
                            for (int i = 0; i < tweetimagelist.stream().count(); i++) {
                                ScreenshotTweetModClient.LOGGER.info("Uploading...[" + i + "/" + (tweetimagelist.stream().count() - 1) + "][" + tweetimagelist.get(i) + "]");
                                media = twitter.uploadMedia(new File(tweetimagelist.get(i)));
                                ScreenshotTweetModClient.LOGGER.info("Uploaded: id=" + media.getMediaId()
                                        + ", w=" + media.getImageWidth() + ", h=" + media.getImageHeight()
                                        + ", type=" + media.getImageType() + ", size=" + media.getSize());
                                mediaIds[i] = media.getMediaId();
                            }
                            update.setMediaIds(mediaIds);
                        }

                        medias.clear();

                        Status status = twitter.updateStatus(update);
                        ScreenshotTweetModClient.LOGGER.info("Successfully updated the status to [" + status.getText() + "].");
                    }
                } else {
                    ScreenshotTweetModClient.LOGGER.info("Failed to update the status");
                }
            }catch (StringIndexOutOfBoundsException ignored) { }
            catch (TwitterException twitterException) {
                ScreenshotTweetModClient.LOGGER.info(twitterException.getErrorMessage());
            }

            tweetimagelist.clear();
            tweetimagedata.clear();

            imagescount.setText(new TranslatableText("screenshottweet.gui.imagescountdefault"));
            buttonManager.setEnabled(true);

            ScreenshotTweetModClient.LOGGER.info("Images Cleared");
        }
        catch (Exception ignored){

        }
    }

    @Override
    public void run() {

    }
}
