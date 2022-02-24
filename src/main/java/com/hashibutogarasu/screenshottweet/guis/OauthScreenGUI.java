package com.hashibutogarasu.screenshottweet.guis;

import com.hashibutogarasu.screenshottweet.Ids.Id;
import com.hashibutogarasu.screenshottweet.Threads.OauthThread;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.text.TranslatableText;

import static com.hashibutogarasu.screenshottweet.Ids.Identifiers.*;

public class OauthScreenGUI extends LightweightGuiDescription {
    public static WButton Oauthbutton = new WButton();
    public static WTextField pinfield = new WTextField();
    public static WLabel pinlabel = new WLabel(new TranslatableText("screenshottweet.gui.oauthscreen.button.pinlabel"));
    public static WGridPanel oauthroot = new WGridPanel();

    public static WSprite oauthstatusimage = new WSprite(none);
    public static WSprite oauthloadingstatusimage = new WSprite(4,frame0,frame1,frame2,frame3,frame4,frame5,frame6,frame7,frame8,frame9,frame10,frame11,frame12,frame13,frame14,frame15);

    public OauthScreenGUI(){
        oauthroot = new WGridPanel();
        oauthroot.setSize(40,30);
        setRootPanel(oauthroot);

        WLabel ScreenTitle = new WLabel(new TranslatableText("screenshottweet.gui.oauthscreen.title"));
        oauthroot.add(ScreenTitle,1,1);

        Oauthbutton = new WButton();

        Oauthbutton.setOnClick(()->{
            OauthThread oauthThread = new OauthThread();
            Thread th = new Thread(oauthThread);
            th.start();
        });

        Oauthbutton.setLabel(new TranslatableText("screenshottweet.gui.oauthscreen.button.oauthbutton"));
        oauthroot.add(Oauthbutton, 1, 2,6,10);

        pinlabel = new WLabel(new TranslatableText("screenshottweet.gui.oauthscreen.button.pinlabel"));
        pinfield = new WTextField();

        oauthroot.add(pinlabel, 1, 4);
        oauthroot.add(pinfield, 1, 5,4,10);

        WLabel loggedaccountlabel = new WLabel(new TranslatableText("screenshottweet.gui.oauthscreen.oauthloggedas", Id.Screenname));
        oauthroot.add(loggedaccountlabel, 1, 7);

    }
}
