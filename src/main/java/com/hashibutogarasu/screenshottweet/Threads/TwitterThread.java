package com.hashibutogarasu.screenshottweet.Threads;

import com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory;
import com.hashibutogarasu.screenshottweet.Custom.CustomTextField;
import com.hashibutogarasu.screenshottweet.Ids.Id;
import com.hashibutogarasu.screenshottweet.ScreenshotTweetModClient;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import net.minecraft.text.TranslatableText;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.*;

import static com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory.twitterkeyconfig;
import static com.hashibutogarasu.screenshottweet.Ids.Id.MOD_ID;
import static com.hashibutogarasu.screenshottweet.Ids.Identifiers.*;
import static com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI.*;

public class TwitterThread extends Thread {
    private final boolean tweet;
    public static WLabel tweetstatuslabel = new WLabel(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel"));
    public static int defaultcolor = tweetstatuslabel.getColor();

    public TwitterThread(boolean tweets){
        tweet = tweets;
    }

    @Override
    public void run(){

        tweettext.setEditable(false);
        tweetbutton.setEnabled(false);

        NetCheck.Checkinternetconnection();
        TextFields.Check(tweet);

        if(!tweet || (tweetimagelist != null && tweetimagedata != null)){
            Default.Set();
        }

        if(!twitterkeyconfig.showscreenname && twitterkeyconfig.showlogsintweetscreen){
            root.add(tweetstatuslabel,13,9);
        }
        else{
            root.add(tweetstatuslabel,13,10);
        }

        loadingstatusimage = new WSprite(4,frame0,frame1,frame2,frame3,frame4,frame5,frame6,frame7,frame8,frame9,frame10,frame11,frame12,frame13,frame14,frame15);
        root.add(loadingstatusimage,19,4,1,1);

        try {
            Tweet.update();
        }
        catch (Exception error){
            tweetstatuslabel.setText(new TranslatableText(MOD_ID + ".gui.tweetstatuslabel.tweet.failed"));
            tweetstatuslabel.setColor(Color.RED.getRGB());
            root.remove(loadingstatusimage);
            statusimage.setImage(FAILED);
            ScreenshotTweetModClient.LOGGER.info(error.toString());
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

        Default.reset();

        try {
            tweettext.refresh();
        }
        catch (InterruptedException e) {

        }

        tweetbutton.setEnabled(true);
        tweettext.setEditable(true);
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

        tweetbutton.setEnabled(true);
    }
}
