package com.hashibutogarasu.screenshottweet.guis;

import com.hashibutogarasu.screenshottweet.FileObjects;
import com.hashibutogarasu.screenshottweet.Images.ScreenshotsGUIScreenLayout;
import com.hashibutogarasu.screenshottweet.Threads.OpenURl;
import com.hashibutogarasu.screenshottweet.Threads.TwitterThread;
import com.hashibutogarasu.screenshottweet.Utils.ButtonManager;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
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

public class TweetScreenGUI extends LightweightGuiDescription
{
    public static String Fullpath = "";
    public static WButton tweetbutton = new WButton();
    public static WLabel imagescount = new WLabel("0");
    public static ArrayList<String> tweetimagedata;
    public static ArrayList<String> tweetimagelist;
    public static ButtonManager buttonManager = new ButtonManager();
    public static WTextField tweettext = new WTextField();

    public TweetScreenGUI(String path)
    {
        FileObjects fileObjects = new FileObjects();
        buttonManager = new ButtonManager();

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(390, 200);

        WLabel ScreenTitle = new WLabel(new TranslatableText("screenshottweet.gui.title"));
        root.add(ScreenTitle,1,1);

        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> imgdata = new ArrayList<>();
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
                if(tweetimagedata.stream().count() <= 3 && tweetimagedata.stream().count() < 4){
                    tweetimagedata.add(FullDir + s);
                    imagescount.setText(Text.of(String.valueOf(tweetimagedata.stream().count())));
                    imgdata.add(s);
                }
                if(tweetimagedata.stream().count() >= 4){
                    buttonManager.setEnabled(false);
                }
            });

            destination.OpenButton.setOnClick(() -> {
                OpenURl threadRun = new OpenURl("file://" + FullDir + s,true);
                Thread th = new Thread(threadRun);
                th.start();
            });
        };

        WListPanel<String, ScreenshotsGUIScreenLayout> list = new WListPanel<>(data, ScreenshotsGUIScreenLayout::new, configurator);
        list.setListItemHeight(2*18);
        root.add(list, 0, 2, 12, 9);

        tweettext = new WTextField();
        tweettext.setMaxLength(100);
        root.add(tweettext, 13, 2, 7, 12);

        tweetbutton = new WButton();
        tweetbutton.setLabel(new TranslatableText("screenshottweet.gui.tweetbutton"));

        tweetbutton.setOnClick(()->{
            TwitterThread threadRun = new TwitterThread(true);
            Thread th = new Thread(threadRun);
            th.start();
        });

        root.add(tweetbutton, 13, 4, 5, 7);

        WButton statusimage = new WButton();
        Icon statusicon = new TextureIcon(new Identifier("screenshottweet:textures/gui/loading.png"));
        statusimage.setIcon(statusicon);
        root.add(statusimage,19,4,1,3);

        WButton clearimagesbutton = new WButton();
        Icon icon = new TextureIcon(new Identifier("screenshottweet:textures/gui/trash.png"));
        clearimagesbutton.setIcon(icon);

        clearimagesbutton.setOnClick(()->{
            TwitterThread threadRun = new TwitterThread(false);
            Thread th = new Thread(threadRun);
            th.start();
        });

        root.add(clearimagesbutton,18,4 , 1 , 3);

        WLabel imagescountlabel = new WLabel(new TranslatableText("screenshottweet.gui.imagescount"));
        root.add(imagescountlabel, 13, 6);

        imagescount = new WLabel("0");
        root.add(imagescount, 17, 6, 7, 7);

        root.validate(this);
    }
}