package com.hashibutogarasu.screenshottweet.Threads;

import com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory;
import com.hashibutogarasu.screenshottweet.Ids.Id;
import com.hashibutogarasu.screenshottweet.ScreenshotTweetModClient;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import net.minecraft.text.TranslatableText;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import static com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory.twitterkeyconfig;
import static com.hashibutogarasu.screenshottweet.Ids.Id.MOD_ID;
import static com.hashibutogarasu.screenshottweet.Ids.Identifiers.*;
import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.*;

public class TwitterThread extends Thread {
    private final boolean tweet;
    private final WLabel tweetstatuslabel = new WLabel(new TranslatableText(MOD_ID + "gui.tweetstatuslabel"));
    private int defaultcolor = 0;
    ArrayList<UploadedMedia> medias = new ArrayList<>();

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

            imagescount.setText(new TranslatableText(MOD_ID + ".gui.imagescountdefault"));
            buttonManager.setEnabled(true);

            return;
        }

        loadingstatusimage = new WSprite(4,frame0,frame1,frame2,frame3,frame4,frame5,frame6,frame7,frame8,frame9,frame10,frame11,frame12,frame13,frame14,frame15);
        root.add(loadingstatusimage,19,4,1,1);

        try {
            tweetimagelist = new ArrayList<>();

            tweetimagelist.addAll(tweetimagedata);

            ScreenshotTweetModClient.LOGGER.info("Tweet:" + tweettext.getText());


            if(!twitterkeyconfig.showscreenname && twitterkeyconfig.showlogsintweetscreen){
                root.add(tweetstatuslabel,13,9);
            }
            else{
                root.add(tweetstatuslabel,13,10);
            }

            defaultcolor = tweetstatuslabel.getColor();

            try {

                tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.oauth"));

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

                if ((long) tweetimagelist.size() != 0) {
                    try {
                        for (String p : tweetimagelist) {
                            medias.add(twitter.uploadMedia(new File(p)));
                        }
                    } catch (TwitterException e) {
                        ScreenshotTweetModClient.LOGGER.info(e.toString());
                    }
                }

                if (tweettext.getText().length() != 0 || (long) tweetimagelist.size() >= 1) {

                    StatusUpdate update = new StatusUpdate(tweettext.getText());

                    long[] mediaIds = new long[tweetimagelist.size()];

                    UploadedMedia media;
                    if ((long) tweetimagelist.size() != 0) {
                        for (int i = 0; (long) tweetimagelist.size() > i; i++) {
                            ScreenshotTweetModClient.LOGGER.info("Uploading...[" + i + "/" + ((long) tweetimagelist.size() - 1) + "][" + tweetimagelist.get(i) + "]");
                            media = twitter.uploadMedia(new File(tweetimagelist.get(i)));
                            ScreenshotTweetModClient.LOGGER.info("Uploaded: id=" + media.getMediaId()
                                    + ", w=" + media.getImageWidth() + ", h=" + media.getImageHeight()
                                    + ", type=" + media.getImageType() + ", size=" + media.getSize());
                            mediaIds[i] = media.getMediaId();

                            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.uploadingimage",i));

                        }
                        update.setMediaIds(mediaIds);
                    }

                    medias.clear();

                    Status status = twitter.updateStatus(update);

                    Thread.sleep(2000);
                    ScreenshotTweetModClient.LOGGER.info("Successfully updated the status to [" + status.getText() + "].");
                    tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.tweet.ok"));
                    tweetstatuslabel.setColor(Color.GREEN.getRGB());
                    root.remove(loadingstatusimage);
                    statusimage.setImage(OK);
                } else {
                    Thread.sleep(2000);
                    ScreenshotTweetModClient.LOGGER.info("Failed to update the status");
                    tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.tweet.failed"));
                    tweetstatuslabel.setColor(Color.RED.getRGB());
                    root.remove(loadingstatusimage);
                    statusimage.setImage(FAILED);
                }
            }catch (StringIndexOutOfBoundsException ignored) { }
            catch (TwitterException twitterException) {
                ScreenshotTweetModClient.LOGGER.info(twitterException.getErrorMessage());
                Thread.sleep(2000);
                tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.tweet.failed"));
                tweetstatuslabel.setColor(Color.RED.getRGB());
                root.remove(loadingstatusimage);
                statusimage.setImage(FAILED);
            }

            tweetimagelist.clear();
            tweetimagedata.clear();

            imagescount.setText(new TranslatableText("screenshottweet.gui.imagescountdefault"));
            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel"));
            buttonManager.setEnabled(true);
        }
        catch (Exception error){
            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.tweet.failed"));
            tweetstatuslabel.setColor(Color.RED.getRGB());
            root.remove(loadingstatusimage);
            statusimage.setImage(FAILED);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

        tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel"));
        tweetstatuslabel.setColor(defaultcolor);
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
        catch (TwitterException | IllegalStateException ignore){

        }
    }
}
