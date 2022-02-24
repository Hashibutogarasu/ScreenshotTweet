package com.hashibutogarasu.screenshottweet.Threads;

import com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory;
import com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI;
import com.hashibutogarasu.screenshottweet.guis.Tweetscreen;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import static com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory.twitterkeyconfig;
import static com.hashibutogarasu.screenshottweet.Ids.Identifiers.*;
import static com.hashibutogarasu.screenshottweet.Ids.Identifiers.frame15;
import static com.hashibutogarasu.screenshottweet.guis.OauthScreenGUI.*;
import static net.minecraft.util.Util.getOperatingSystem;

public class OauthThread extends Thread{

    private RequestToken requestToken;

    public OauthThread() { }

    @Override
    public void run(){
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitterkeyconfig.apikey)
                .setOAuthConsumerSecret(twitterkeyconfig.apikeysecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            requestToken = twitter.getOAuthRequestToken();
            getOperatingSystem().open(requestToken.getAuthorizationURL());

            Oauthbutton.setLabel(new TranslatableText("screenshottweet.gui.oauthscreen.button.oauthbutton.oauth"));

            Oauthbutton.setOnClick(() -> {
                try{
                    oauthstatusimage = new WSprite(none);
                    oauthloadingstatusimage = new WSprite(4,frame0,frame1,frame2,frame3,frame4,frame5,frame6,frame7,frame8,frame9,frame10,frame11,frame12,frame13,frame14,frame15);

                    oauthroot.add(oauthstatusimage,5,5,1,1);
                    oauthroot.add(oauthloadingstatusimage,5,5,1,1);

                    String pin = pinfield.getText();

                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    twitter.setOAuthAccessToken(accessToken);

                    twitterkeyconfig.accesstoken = twitter.getOAuthAccessToken().getToken();
                    twitterkeyconfig.accesstokensecret = twitter.getOAuthAccessToken().getTokenSecret();
                    twitterkeyconfig.save();
                    twitterkeyconfig.load();

                    TwitterThread.Oauth();

                    oauthroot.remove(oauthloadingstatusimage);

                    Thread.sleep(1000);

                    oauthstatusimage.setImage(OK);

                    Thread.sleep(2000);

                    MinecraftClient.getInstance().setScreen(new Tweetscreen(new TweetScreenGUI()));

                }
                catch (TwitterException e) {
                    oauthstatusimage.setImage(FAILED);
                } catch (InterruptedException e) { }
            });
        } catch (TwitterException e) {
            oauthstatusimage.setImage(FAILED);
        }

        oauthroot.remove(oauthstatusimage);
        oauthroot.remove(oauthloadingstatusimage);
    }
}
