package com.Hashibutogarasu.screenshottweet.guis.Config;

import com.Hashibutogarasu.screenshottweet.ScreenshotTweetMod;
import com.Hashibutogarasu.screenshottweet.guis.TweetScreenGUI;
import com.Hashibutogarasu.screenshottweet.guis.Tweetscreen;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import twitter4j.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

public class KeySettingScreenGUI extends LightweightGuiDescription
{
    private String keyfilepath = "twitterkeys.txt";
    private WLabel LoginStatus = new WLabel("");

    public KeySettingScreenGUI()
    {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(390, 200);

        Read(keyfilepath);

        WLabel ApikeyLabel = new WLabel(new TranslatableText("screenshottweet.gui.apikey"));
        root.add(ApikeyLabel,1,1);

        WTextField ApikeyText = new WTextField();
        ApikeyText.setMaxLength(99999);
        ApikeyText.setText(TwitterKeyObject.Apikey);
        root.add(ApikeyText,1,2,7,2);

        WLabel ApikeySecretLabel = new WLabel(new TranslatableText("screenshottweet.gui.apikeysecret"));
        root.add(ApikeySecretLabel,1,4);

        WTextField ApikeySecretText = new WTextField();
        ApikeySecretText.setMaxLength(99999);
        ApikeySecretText.setText(TwitterKeyObject.ApikeySecret);
        root.add(ApikeySecretText,1,5,7,2);

        WLabel AccessTokenLabel = new WLabel(new TranslatableText("screenshottweet.gui.accesstoken"));
        root.add(AccessTokenLabel,9,1);

        WTextField AccessTokenText = new WTextField();
        AccessTokenText.setMaxLength(99999);
        AccessTokenText.setText(TwitterKeyObject.AccessToken);
        root.add(AccessTokenText,9,2,7,2);

        WLabel AccessTokenSecretLabel = new WLabel(new TranslatableText("screenshottweet.gui.accesstokensecret"));
        root.add(AccessTokenSecretLabel,9,4);

        WTextField AccessTokenSecretText = new WTextField();
        AccessTokenSecretText.setMaxLength(99999);
        AccessTokenSecretText.setText(TwitterKeyObject.AccessTokenSecret);
        root.add(AccessTokenSecretText,9,5,7,2);

        WButton CancelAndBackButton = new WButton(new TranslatableText("screenshottweet.gui.cancelandback"));

        CancelAndBackButton.setOnClick(()->{
            MinecraftClient.getInstance().setScreen(new Tweetscreen(new TweetScreenGUI(Paths.get("screenshots").toAbsolutePath().toString())));
        });

        root.add(CancelAndBackButton,1,7,5,2);

        WButton OauthButton = new WButton(new TranslatableText("screenshottweet.gui.oauthandsave"));

        OauthButton.setOnClick(()->{
            SaveKeys(
                    ApikeyText.getText(),
                    ApikeySecretText.getText(),
                    AccessTokenText.getText(),
                    AccessTokenSecretText.getText(),
                    keyfilepath);

            OauthKeys(
                    ApikeyText.getText(),
                    ApikeySecretText.getText(),
                    AccessTokenText.getText(),
                    AccessTokenSecretText.getText(),
                    keyfilepath);
        });

        root.add(OauthButton,7,7,5,2);

        LoginStatus = new WLabel("");
        root.add(LoginStatus,13,7);

        root.validate(this);
    }

    public static void Read(String filepath){

        String[] keys = new String[]{"ApiKey","ApiKeySecret","AccessToken","AccessTokenSecret"};

        if(new File(filepath).exists()){
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(filepath))){
                String res = reader.readLine();
                if(res != null){
                    keys = res.split(",");
                }
            } catch (IOException e) {

            }
        }
        else{
            keys[0] = "ApiKey";
            keys[1] = "ApiKeySecret";
            keys[2] = "AccessToken";
            keys[3] = "AccessTokenSecret";

            try {
                FileWriter filewriter = new FileWriter(new File(filepath));
                filewriter.write("ApiKey,ApiKeySecret,AccessToken,AccessTokenSecret");
                filewriter.close();
            } catch (IOException e) {

            }
        }

        TwitterKeyObject.Apikey = keys[0];
        TwitterKeyObject.ApikeySecret = keys[1];
        TwitterKeyObject.AccessToken = keys[2];
        TwitterKeyObject.AccessTokenSecret = keys[3];

        if(TwitterKeyObject.Apikey == null || TwitterKeyObject.Apikey.isEmpty() || TwitterKeyObject.Apikey.isBlank()){
            TwitterKeyObject.Apikey = "";
        }

        if(TwitterKeyObject.ApikeySecret == null || TwitterKeyObject.ApikeySecret.isEmpty() || TwitterKeyObject.ApikeySecret.isBlank()){
            TwitterKeyObject.ApikeySecret = "";
        }

        if(TwitterKeyObject.AccessToken == null || TwitterKeyObject.AccessToken.isEmpty() || TwitterKeyObject.AccessToken.isBlank()){
            TwitterKeyObject.AccessToken = "";
        }

        if(TwitterKeyObject.AccessTokenSecret == null || TwitterKeyObject.AccessTokenSecret.isEmpty() || TwitterKeyObject.AccessTokenSecret.isBlank()){
            TwitterKeyObject.AccessTokenSecret = "";
        }
    }

    private void SaveKeys(String Apikey, String ApikeySecret, String AccessToken, String AccessTokenSecret,String filepath)
    {
        if(Apikey == null || Apikey.isBlank() || Apikey.isEmpty()){
            Apikey = "Apikey";
        }

        if(ApikeySecret == null || ApikeySecret.isBlank() || ApikeySecret.isEmpty()){
            ApikeySecret = "ApikeySecret";
        }

        if(AccessToken == null || AccessToken.isBlank() || AccessToken.isEmpty()){
            AccessToken = "AccessToken";
        }

        if(AccessTokenSecret == null || AccessTokenSecret.isBlank() || AccessTokenSecret.isEmpty()){
            AccessTokenSecret = "AccessTokenSecret";
        }

        String res = Apikey + "," + ApikeySecret + "," + AccessToken + "," + AccessTokenSecret;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filepath))){
            writer.write(res);
        }catch (Exception e){

        }
    }

    private void OauthKeys(String Apikey, String ApikeySecret, String AccessToken, String AccessTokenSecret,String filepath)
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Apikey)
                .setOAuthConsumerSecret(ApikeySecret)
                .setOAuthAccessToken(AccessToken)
                .setOAuthAccessTokenSecret(AccessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            ScreenshotTweetMod.LOGGER.info("Logged in as " + twitter.getScreenName());

            LoginStatus.setText(new TranslatableText("screenshottweet.gui.loginsuccess",twitter.getScreenName()));

            TwitterKeyObject.Apikey = Apikey;
            TwitterKeyObject.ApikeySecret = ApikeySecret;
            TwitterKeyObject.AccessToken = AccessToken;
            TwitterKeyObject.AccessTokenSecret = AccessTokenSecret;

        } catch (TwitterException e) {

            TwitterKeyObject.Apikey = "Apikey";
            TwitterKeyObject.ApikeySecret = "ApikeySecret";
            TwitterKeyObject.AccessToken = "AccessToken";
            TwitterKeyObject.AccessTokenSecret = "AccessTokenSecret";

            ScreenshotTweetMod.LOGGER.info("Failed to Oauth");
            ScreenshotTweetMod.LOGGER.info(e.toString());
            LoginStatus.setText(new TranslatableText("screenshottweet.gui.loginfailed"));
        }
    }
}