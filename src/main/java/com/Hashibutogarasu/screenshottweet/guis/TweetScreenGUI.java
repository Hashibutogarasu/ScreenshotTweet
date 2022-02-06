package com.Hashibutogarasu.screenshottweet.guis;

import com.Hashibutogarasu.screenshottweet.FileObjects;
import com.Hashibutogarasu.screenshottweet.Images.ScreenshotsGUIScreenLayout;
import com.Hashibutogarasu.screenshottweet.ScreenshotTweetMod;
import com.Hashibutogarasu.screenshottweet.Threads.ThreadRun;
import com.Hashibutogarasu.screenshottweet.Utils.ButtonManager;
import com.Hashibutogarasu.screenshottweet.guis.Config.KeySettingScreen;
import com.Hashibutogarasu.screenshottweet.guis.Config.KeySettingScreenGUI;
import com.Hashibutogarasu.screenshottweet.guis.Config.TwitterKeyObject;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class TweetScreenGUI extends LightweightGuiDescription
{
    private String Fullpath = "";
    private WButton tweetbutton = new WButton();
    private WLabel imagescount = new WLabel("0");
    private ArrayList<String> tweetimagedata;
    private ArrayList<String> tweetimagelist;

    public TweetScreenGUI(String path)
    {
        KeySettingScreenGUI.Read("twitterkeys.txt");

        ScreenshotTweetMod.LOGGER.info(TwitterKeyObject.Apikey);

        FileObjects fileObjects = new FileObjects();
        ButtonManager buttonManager = new ButtonManager();

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(390, 200);

        WLabel ScreenTitle = new WLabel(new TranslatableText("screenshottweet.gui.title"));
        root.add(ScreenTitle,1,1);

        ArrayList<String> data = new ArrayList<>();
        tweetimagedata = new ArrayList<>();

        try(Stream<Path> stream = Files.list(Paths.get("screenshots").toAbsolutePath())) {
            stream.forEach(p -> {
                data.add(p.getFileName().toString());
                fileObjects.FileName.add(p.getFileName().toString());
                fileObjects.FullPath.add(p.toFile().getAbsolutePath());
            });
        }catch(IOException e) {

        }

        BiConsumer<String, ScreenshotsGUIScreenLayout> configurator = (String s, ScreenshotsGUIScreenLayout destination) -> {

            Path imagepath = Paths.get("screenshots" + s);
            String imagedir = Paths.get("screenshots").toAbsolutePath().toString();
            Fullpath = (imagedir + "/" + imagepath.getFileName().toString().replace("screenshots","/")).replace("//","/").replace("\\","/");
            String FullDir = Fullpath.replace(Paths.get(Fullpath).getFileName().toString(),"");

            destination.label.setText(new LiteralText(s));
            destination.AddButton.setLabel(new TranslatableText("screenshottweet.gui.addimage"));
            destination.OpenButton.setLabel(new TranslatableText("screenshottweet.gui.openimage"));

            buttonManager.add(destination.AddButton);

            destination.AddButton.setOnClick(() -> {
                imagescount.setText(Text.of(String.valueOf(tweetimagedata.stream().count() + 1)));
                if(tweetimagedata.stream().count() <= 3){
                    tweetimagedata.add(FullDir + s);
                }
                else{ }
                if(tweetimagedata.stream().count() + 1 >= 5){
                    buttonManager.setEnabled(false);
                }
            });

            destination.OpenButton.setOnClick(() -> {
                ThreadRun threadRun = new ThreadRun(FullDir + s);
                Thread th = new Thread(threadRun);
                th.start();
            });
        };

        WListPanel<String, ScreenshotsGUIScreenLayout> list = new WListPanel<>(data, ScreenshotsGUIScreenLayout::new, configurator);
        list.setListItemHeight(2*18);
        root.add(list, 0, 2, 12, 9);

        WTextField tweettext = new WTextField();
        tweettext.setMaxLength(1000);
        root.add(tweettext, 13, 2, 7, 7);

        tweetbutton = new WButton();
        tweetbutton.setLabel(new TranslatableText("screenshottweet.gui.tweetbutton"));

        tweetbutton.setOnClick(()->{
            try {

                tweetimagelist = new ArrayList<>();
                String TweetText = tweettext.getText();

                for (String imagespath : tweetimagedata) {
                    if (!imagespath.isBlank() || !imagespath.isEmpty() || imagespath != null) {
                        tweetimagelist.add(imagespath);
                    }
                }

                ScreenshotTweetMod.LOGGER.info("Tweet:" + TweetText);

                for (String imagelist : tweetimagelist) {
                    ScreenshotTweetMod.LOGGER.info(imagelist);
                }

                try {
                    ConfigurationBuilder cb = new ConfigurationBuilder();

                /*cb.setDebugEnabled(true)
                        .setOAuthConsumerKey("n9dmocY4Qo76h9wtbyuSUpgdt")
                        .setOAuthConsumerSecret("wz221zVP98UlydbwhD8y8yGhztZ2B9dSzUjgGfdNRTaF5gUwiB")
                        .setOAuthAccessToken("1122424030307295233-rgNoeDcgTbsaerf6AUXuPVu7TKWImb")
                        .setOAuthAccessTokenSecret("UUSCNmchp5DbSvXzRBZwBjj4N0SmHg3VOaQ7Awh8EzaUb");

                */

                    cb.setDebugEnabled(true)
                            .setOAuthConsumerKey(TwitterKeyObject.Apikey)
                            .setOAuthConsumerSecret(TwitterKeyObject.ApikeySecret)
                            .setOAuthAccessToken(TwitterKeyObject.AccessToken)
                            .setOAuthAccessTokenSecret(TwitterKeyObject.AccessTokenSecret);

                    TwitterFactory tf = new TwitterFactory(cb.build());
                    Twitter twitter = tf.getInstance();

                    ScreenshotTweetMod.LOGGER.info("Logged in as:" + twitter.getScreenName());

                    ArrayList<UploadedMedia> medias = new ArrayList<>();

                    if (tweetimagelist.stream().count() != 0) {
                        try {
                            for (String p : tweetimagelist) {
                                medias.add(twitter.uploadMedia(new File(p)));
                            }
                        } catch (TwitterException e) {
                            ScreenshotTweetMod.LOGGER.info(e.toString());
                        }
                    }

                    if (TweetText.length() != 0 || tweetimagelist.stream().count() >= 1) {

                        String twtext = "";

                        if(TweetText.length() != 0){
                            twtext = TweetText;
                        }

                        StatusUpdate update = new StatusUpdate(twtext);

                        long[] mediaIds = new long[(int) tweetimagelist.stream().count()];

                        UploadedMedia media;

                        if (tweetimagelist.stream().count() != 0) {
                            for (int i = 0; i < tweetimagelist.stream().count(); i++) {
                                ScreenshotTweetMod.LOGGER.info("Uploading...[" + i + "/" + (tweetimagelist.stream().count() - 1) + "][" + tweetimagelist.get(i) + "]");
                                media = twitter.uploadMedia(new File(tweetimagelist.get(i)));
                                ScreenshotTweetMod.LOGGER.info("Uploaded: id=" + media.getMediaId()
                                        + ", w=" + media.getImageWidth() + ", h=" + media.getImageHeight()
                                        + ", type=" + media.getImageType() + ", size=" + media.getSize());
                                mediaIds[i] = media.getMediaId();
                            }
                            update.setMediaIds(mediaIds);
                        }

                        Status status = twitter.updateStatus(update);
                        ScreenshotTweetMod.LOGGER.info("Successfully updated the status to [" + status.getText() + "].");
                    } else {
                        ScreenshotTweetMod.LOGGER.info("Failed to update the status");
                    }
                } catch (TwitterException te) { }

                try {
                    if(tweetimagelist.stream().count() != 0 && tweetimagedata.stream().count() != 0){
                        tweetimagelist.clear();
                        tweetimagedata.clear();
                    }

                    tweettext.setText("");
                    imagescount.setText(new TranslatableText("screenshottweet.gui.imagescountdefault"));
                    WButton[] buttonenabled = buttonManager.getEnabled();

                    for (WButton button : buttonenabled) {
                        if(!button.isEnabled()){
                            button.setEnabled(true);
                        }
                    }

                    ScreenshotTweetMod.LOGGER.info("Images Cleared");

                } catch (StringIndexOutOfBoundsException e) {

                } catch (Exception e) {

                }
            }
            catch (Exception e){

            }
        });

        root.add(tweetbutton, 13, 4, 6, 7);

        WButton clearimagesbutton = new WButton();
        Icon icon = new TextureIcon(new Identifier("screenshottweet:textures/gui/trash.png"));
        clearimagesbutton.setIcon(icon);

        clearimagesbutton.setOnClick(()->{
            try {
                if(tweetimagelist.stream().count() != 0 && tweetimagedata.stream().count() != 0){
                    tweetimagelist.clear();
                    tweetimagedata.clear();
                }

                tweettext.setText("");
                imagescount.setText(new TranslatableText("screenshottweet.gui.imagescountdefault"));
                WButton[] buttonenabled = buttonManager.getEnabled();

                for (WButton button : buttonenabled) {
                    if(!button.isEnabled()){
                        button.setEnabled(true);
                    }
                }

                ScreenshotTweetMod.LOGGER.info("Images Cleared");

            } catch (StringIndexOutOfBoundsException e) {

            } catch (Exception e) {

            }
        });

        root.add(clearimagesbutton,19 ,4 , 1 , 3);

        WButton settingsbutton = new WButton();
        settingsbutton.setLabel(new TranslatableText("screenshottweet.gui.settingsbutton"));

        settingsbutton.setOnClick(()->{
            MinecraftClient.getInstance().setScreen(new KeySettingScreen(new KeySettingScreenGUI()));//GUI呼び出し
        });

        root.add(settingsbutton, 13, 6, 7, 7);

        WLabel imagescountlabel = new WLabel(new TranslatableText("screenshottweet.gui.imagescount"));
        root.add(imagescountlabel, 13, 8);

        imagescount = new WLabel("0");
        root.add(imagescount, 17, 8, 7, 7);

        root.validate(this);
    }
}