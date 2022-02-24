package com.hashibutogarasu.screenshottweet.Threads;

import com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory;
import com.hashibutogarasu.screenshottweet.Ids.Id;
import com.hashibutogarasu.screenshottweet.ScreenshotTweetModClient;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.TranslatableText;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.util.ArrayList;

import static com.hashibutogarasu.screenshottweet.Ids.Identifiers.*;
import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.*;

public class TwitterThread extends Thread {
    private boolean tweet = false;

    public TwitterThread(boolean tweets){
        tweet = tweets;
    }

    @Override
    public void run(){
        if(!tweet || (tweetimagelist != null && tweetimagedata != null)){

            if(tweetimagelist != null && tweetimagedata != null){
                tweetimagelist.clear();
                tweetimagedata.clear();
            }

            imagescount.setText(new TranslatableText("screenshottweet.gui.imagescountdefault"));
            buttonManager.setEnabled(true);

            return;
        }

        loadingstatusimage = new WSprite(4,frame0,frame1,frame2,frame3,frame4,frame5,frame6,frame7,frame8,frame9,frame10,frame11,frame12,frame13,frame14,frame15);
        root.add(loadingstatusimage,19,4,1,1);

        try {
            tweetimagelist = new ArrayList<>();

            for (String imagespath : tweetimagedata) {
                if (!imagespath.isBlank() || !imagespath.isEmpty() || imagespath != null) {
                    tweetimagelist.add(imagespath);
                }
            }

            ScreenshotTweetModClient.LOGGER.info("Tweet:" + tweettext.getText());

            try {
                ConfigurationBuilder cb = new ConfigurationBuilder();

                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.apikey)
                        .setOAuthConsumerSecret(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.apikeysecret)
                        .setOAuthAccessToken(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.accesstoken)
                        .setOAuthAccessTokenSecret(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.accesstokensecret);

                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();

                Id.Screenname = twitter.getScreenName();

                ScreenshotTweetModClient.LOGGER.info("Logged in as:" + Id.Screenname);

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

                        Thread.sleep(2000);
                        root.remove(loadingstatusimage);
                        statusimage.setImage(OK);
                    }
                } else {
                    ScreenshotTweetModClient.LOGGER.info("Failed to update the status");
                    Thread.sleep(2000);
                    root.remove(loadingstatusimage);
                    statusimage.setImage(FAILED);
                }
            }catch (StringIndexOutOfBoundsException ignored) { }
            catch (TwitterException twitterException) {
                ScreenshotTweetModClient.LOGGER.info(twitterException.getErrorMessage());
                Thread.sleep(2000);
                root.remove(loadingstatusimage);
                statusimage.setImage(FAILED);
            }

            tweetimagelist.clear();
            tweetimagedata.clear();

            imagescount.setText(new TranslatableText("screenshottweet.gui.imagescountdefault"));
            buttonManager.setEnabled(true);
        }
        catch (Exception error){
            root.remove(loadingstatusimage);
            statusimage.setImage(FAILED);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

        root.remove(loadingstatusimage);
        statusimage.setImage(none);
    }
    public static void Oauth(){
        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();

            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.apikey)
                    .setOAuthConsumerSecret(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.apikeysecret)
                    .setOAuthAccessToken(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.accesstoken)
                    .setOAuthAccessTokenSecret(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.accesstokensecret);

            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            Id.Screenname = twitter.getScreenName();

            ScreenshotTweetModClient.LOGGER.info("Logged in as:" + Id.Screenname);
        }
        catch (TwitterException e){

        }
        catch(IllegalStateException e){

        }
    }
}
