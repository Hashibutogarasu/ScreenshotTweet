package com.hashibutogarasu.screenshottweet.Threads;

import net.minecraft.util.Util;

public class OpenURl implements Runnable{

    public OpenURl(String uri,boolean openInBrowser){
        if (openInBrowser) {
            Util.getOperatingSystem().open(uri);
        }
    }

    public void run(){

    }
}