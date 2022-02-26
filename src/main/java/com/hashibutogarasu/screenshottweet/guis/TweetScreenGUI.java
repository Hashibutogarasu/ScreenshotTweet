package com.hashibutogarasu.screenshottweet.guis;

import com.hashibutogarasu.screenshottweet.Custom.CustomTextField;
import com.hashibutogarasu.screenshottweet.FileObjects;
import com.hashibutogarasu.screenshottweet.Ids.Id;
import com.hashibutogarasu.screenshottweet.Images.ScreenshotsGUIScreenLayout;
import com.hashibutogarasu.screenshottweet.Threads.Default;
import com.hashibutogarasu.screenshottweet.Threads.OpenURl;
import com.hashibutogarasu.screenshottweet.Threads.TwitterThread;
import com.hashibutogarasu.screenshottweet.Utils.ButtonManager;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory.twitterkeyconfig;
import static com.hashibutogarasu.screenshottweet.Ids.Id.MOD_ID;
import static com.hashibutogarasu.screenshottweet.Ids.Identifiers.*;

public class TweetScreenGUI extends LightweightGuiDescription
{
    public static String Fullpath = "";
    public static WButton tweetbutton = new WButton();
    public static WLabel imagescount = new WLabel("0");
    public static ArrayList<String> tweetimagedata;
    public static ArrayList<String> tweetimagelist;
    public static ButtonManager buttonManager = new ButtonManager();
    public static CustomTextField tweettext = new CustomTextField();
    public static WSprite loadingstatusimage = new WSprite(4,frame0,frame1,frame2,frame3,frame4,frame5,frame6,frame7,frame8,frame9,frame10,frame11,frame12,frame13,frame14,frame15);
    public static WSprite statusimage = new WSprite(none);
    public static WGridPanel root = new WGridPanel();
    ArrayList<String> data;
    ArrayList<String> imgdata;

    public TweetScreenGUI()
    {
        FileObjects fileObjects = new FileObjects();
        buttonManager = new ButtonManager();

        root = new WGridPanel();
        setRootPanel(root);

        data = new ArrayList<>();
        imgdata = new ArrayList<>();
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

            ScreenshotsGUIScreenLayout.label.setText(new LiteralText(s));
            ScreenshotsGUIScreenLayout.AddButton.setLabel(new TranslatableText(MOD_ID + ".gui.tweetscreen.button.addimage"));
            ScreenshotsGUIScreenLayout.OpenButton.setLabel(new TranslatableText(MOD_ID + ".gui.tweetscreen.button.openimage"));

            buttonManager.add(ScreenshotsGUIScreenLayout.AddButton);

            ScreenshotsGUIScreenLayout.AddButton.setOnClick(() -> {
                if(tweetimagedata.size() <= 3){
                    tweetimagedata.add(FullDir + s);
                    imagescount.setText(Text.of(String.valueOf((long) tweetimagedata.size())));
                    imgdata.add(s);
                }
                if((long) tweetimagedata.size() >= 4){
                    buttonManager.setEnabled(false);
                }
            });

            ScreenshotsGUIScreenLayout.OpenButton.setOnClick(() -> {
                OpenURl threadRun = new OpenURl("file://" + FullDir + s,true);
                Thread th = new Thread(threadRun);
                th.start();
            });
        };

        WListPanel<String, ScreenshotsGUIScreenLayout> list = new WListPanel<>(data, ScreenshotsGUIScreenLayout::new, configurator);
        list.setListItemHeight(2*18);
        root.add(list, 0, 2, 12, 9);

        tweettext = new CustomTextField();
        tweettext.setMaxLength(140);

        root.add(tweettext, 13, 2, 7, 12);

        statusimage = new WSprite(none);
        root.add(statusimage,19,4,1,1);

        tweetbutton = new WButton();
        tweetbutton.setLabel(new TranslatableText(MOD_ID + ".gui.tweetscreen.button.tweetbutton"));

        tweetbutton.setOnClick(()->{
            TwitterThread tweetThread = new TwitterThread(true);
            Thread th = new Thread(tweetThread);
            th.start();
        });

        root.add(tweetbutton, 13, 4, 5, 7);

        WButton clearimagesbutton = new WButton();
        Icon icon = new TextureIcon(new Identifier(MOD_ID + ":textures/gui/trash.png"));
        clearimagesbutton.setIcon(icon);

        clearimagesbutton.setOnClick(Default::reset);

        root.add(clearimagesbutton,18,4 , 1 , 3);

        WButton oauthsetting = new WButton();
        oauthsetting.setLabel(new TranslatableText(MOD_ID + ".gui.tweetscreen.button.oauth"));

        oauthsetting.setOnClick(()-> MinecraftClient.getInstance().setScreen(new OauthScreen(new OauthScreenGUI())));

        root.add(oauthsetting, 13, 6,7,10);

        WLabel imagescountlabel = new WLabel(new TranslatableText(MOD_ID + ".gui.tweetscreen.label.imagescount"));
        root.add(imagescountlabel, 13, 8);

        imagescount = new WLabel("0");
        root.add(imagescount, 15, 8, 7, 7);

        if(twitterkeyconfig.showscreenname){
            WLabel loggedaccountlabel = new WLabel(new TranslatableText(MOD_ID + ".gui.oauthscreen.loggedas", Id.Screenname));
            root.add(loggedaccountlabel, 13, 9);
        }

        root.validate(this);
    }
}