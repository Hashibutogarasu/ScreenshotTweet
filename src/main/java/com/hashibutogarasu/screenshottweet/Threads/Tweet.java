package com.hashibutogarasu.screenshottweet.Threads;

import com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory;
import com.hashibutogarasu.screenshottweet.Ids.Id;
import com.hashibutogarasu.screenshottweet.ScreenshotTweetModClient;
import net.minecraft.text.TranslatableText;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import static com.hashibutogarasu.screenshottweet.Ids.Id.MOD_ID;
import static com.hashibutogarasu.screenshottweet.Ids.Identifiers.FAILED;
import static com.hashibutogarasu.screenshottweet.Ids.Identifiers.OK;
import static com.hashibutogarasu.screenshottweet.Threads.TwitterThread.tweetstatuslabel;
import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.*;
import static com.hashibutogarasu.screenshottweet.Threads.TwitterThread.defaultcolor;

public class Tweet {

    static ArrayList<UploadedMedia> medias;

    public static void update() throws InterruptedException {

        tweetimagelist = new ArrayList<>();
        tweetimagelist.addAll(tweetimagedata);

        ScreenshotTweetModClient.LOGGER.info("Tweet:" + tweettext.getText());

        medias = new ArrayList<>();

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

            Thread.sleep(1000);

            tweetstatuslabel.setColor(Color.GREEN.getRGB());
            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.oauth.ok"));

            Thread.sleep(1000);

            tweetstatuslabel.setColor(defaultcolor);
            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.tweeting"));

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

            Thread.sleep(1000);

            if (tweettext.getText().length() != 0 || (long) tweetimagelist.size() >= 1) {

                StatusUpdate update = new StatusUpdate(tweettext.getText());

                long[] mediaIds = new long[tweetimagelist.size()];

                UploadedMedia media;
                if (tweetimagelist.size() != 0) {
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

                Thread.sleep(1000);

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
        }catch (StringIndexOutOfBoundsException | InterruptedException ignored) { }
        catch (TwitterException twitterException) {
            ScreenshotTweetModClient.LOGGER.info(twitterException.getErrorMessage());
            Thread.sleep(2000);
            ScreenshotTweetModClient.LOGGER.info(twitterException.toString());
            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.tweet.failed"));
            tweetstatuslabel.setColor(Color.RED.getRGB());
            root.remove(loadingstatusimage);
            statusimage.setImage(FAILED);
        }

        tweetimagelist.clear();
        tweetimagedata.clear();
        medias.clear();

        imagescount.setText(new TranslatableText("screenshottweet.gui.imagescountdefault"));
        buttonManager.setEnabled(true);
    }
}
